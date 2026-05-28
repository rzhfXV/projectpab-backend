package com.kel6.booking.Service;

import com.kel6.booking.DTO.request.BookingRequest;
import com.kel6.booking.DTO.response.BookingResponse;
import com.kel6.booking.Exception.BusinessException;
import com.kel6.booking.Exception.ResourceNotFoundException;
import com.kel6.booking.Model.*;
import com.kel6.booking.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CourtRepository courtRepository;
    private final CourtScheduleRepository courtScheduleRepository;
    private final NotificationRepository notificationRepository;

    // ─── Buat booking baru ────────────────────────────────────────────
    @Transactional
    public BookingResponse createBooking(BookingRequest request, User user) {

        // 1. Validasi lapangan
        Court court = courtRepository.findById(request.getCourtId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lapangan dengan id " + request.getCourtId() + " tidak ditemukan"));

        if (!court.isActive()) {
            throw new BusinessException("Lapangan ini sedang tidak aktif");
        }

        // 2. Validasi tanggal tidak di masa lalu
        if (request.getBookingDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Tanggal booking tidak boleh di masa lalu");
        }

        // 3. Validasi jam: startTime harus sebelum endTime
        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new BusinessException("Jam mulai harus sebelum jam selesai");
        }

        // 4. Validasi minimum durasi 1 jam
        long durationMinutes = Duration.between(
                request.getStartTime(), request.getEndTime()).toMinutes();
        if (durationMinutes < 60) {
            throw new BusinessException("Durasi minimal booking adalah 1 jam");
        }

        // 5. Validasi jam dalam jam operasional lapangan
        int javaDayValue = request.getBookingDate().getDayOfWeek().getValue();
        int ourDayOfWeek = (javaDayValue % 7);

        CourtSchedule schedule = courtScheduleRepository
                .findByCourtIdAndDayOfWeekAndIsActiveTrue(court.getId(), ourDayOfWeek)
                .orElseThrow(() -> new BusinessException(
                        "Lapangan tidak beroperasi pada hari tersebut"));

        if (request.getStartTime().isBefore(schedule.getOpenTime()) ||
                request.getEndTime().isAfter(schedule.getCloseTime())) {
            throw new BusinessException(
                    "Jam booking di luar jam operasional lapangan (" +
                            schedule.getOpenTime() + " - " + schedule.getCloseTime() + ")");
        }

        // 6. Cek konflik jadwal (query Q1 dari schema.sql)
        long conflict = bookingRepository.countConflictingBookings(
                court.getId(),
                request.getBookingDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (conflict > 0) {
            throw new BusinessException(
                    "Slot waktu ini sudah dipesan. Silakan pilih waktu lain.");
        }

        // 7. Hitung durasi dan total harga
        BigDecimal durationHours = BigDecimal.valueOf(durationMinutes)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        BigDecimal totalPrice = court.getPricePerHour().multiply(durationHours);

        // 8. Buat booking
        Booking booking = Booking.builder()
                .user(user)
                .court(court)
                .bookingDate(request.getBookingDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .durationHours(durationHours)
                .totalPrice(totalPrice)
                .status(Booking.BookingStatus.PENDING)
                .notes(request.getNotes())
                .qrToken(UUID.randomUUID().toString())
                .build();

        Booking saved = bookingRepository.save(booking);

        // 9. Kirim notifikasi ke user
        sendNotification(user,
                "Booking Berhasil Dibuat",
                "Booking lapangan " + court.getName() + " pada " +
                        request.getBookingDate() + " pukul " + request.getStartTime() +
                        " menunggu pembayaran.",
                Notification.NotificationType.BOOKING_CONFIRMED);

        return BookingResponse.from(saved);
    }

    // ─── Riwayat booking milik user ───────────────────────────────────
    public List<BookingResponse> getMyBookings(Long userId) {
        return bookingRepository
                .findByUserIdOrderByBookingDateDescStartTimeDesc(userId)
                .stream()
                .map(BookingResponse::from)
                .collect(Collectors.toList());
    }

    // ─── Detail satu booking ──────────────────────────────────────────
    public BookingResponse getBookingById(Long bookingId, User user) {
        Booking booking = findBookingAndValidateOwner(bookingId, user);
        return BookingResponse.from(booking);
    }

    // ─── User cancel booking ──────────────────────────────────────────
    @Transactional
    public BookingResponse cancelBooking(Long bookingId, User user) {
        Booking booking = findBookingAndValidateOwner(bookingId, user);

        // Hanya bisa cancel kalau status PENDING atau WAITING_PAYMENT
        if (booking.getStatus() != Booking.BookingStatus.PENDING &&
                booking.getStatus() != Booking.BookingStatus.WAITING_PAYMENT) {
            throw new BusinessException(
                    "Booking dengan status " + booking.getStatus() + " tidak bisa dibatalkan");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        Booking saved = bookingRepository.save(booking);

        sendNotification(user,
                "Booking Dibatalkan",
                "Booking lapangan " + booking.getCourt().getName() +
                        " pada " + booking.getBookingDate() + " telah dibatalkan.",
                Notification.NotificationType.BOOKING_CANCELLED);

        return BookingResponse.from(saved);
    }

    // ─── Admin: semua booking dengan filter status ────────────────────
    public List<BookingResponse> getAllBookings(String status) {
        List<Booking> bookings;
        if (status != null && !status.isBlank()) {
            try {
                Booking.BookingStatus bookingStatus =
                        Booking.BookingStatus.valueOf(status.toUpperCase());
                bookings = bookingRepository.findByStatusOrderByCreatedAtAsc(bookingStatus);
            } catch (IllegalArgumentException e) {
                throw new BusinessException("Status tidak valid: " + status);
            }
        } else {
            bookings = bookingRepository.findAll();
        }
        return bookings.stream().map(BookingResponse::from).collect(Collectors.toList());
    }

    // ─── Admin: konfirmasi booking ────────────────────────────────────
    @Transactional
    public BookingResponse confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking tidak ditemukan"));

        if (booking.getStatus() != Booking.BookingStatus.WAITING_PAYMENT) {
            throw new BusinessException(
                    "Hanya booking berstatus WAITING_PAYMENT yang bisa dikonfirmasi");
        }

        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        Booking saved = bookingRepository.save(booking);

        sendNotification(booking.getUser(),
                "Booking Dikonfirmasi!",
                "Booking lapangan " + booking.getCourt().getName() +
                        " pada " + booking.getBookingDate() + " pukul " +
                        booking.getStartTime() + " telah dikonfirmasi.",
                Notification.NotificationType.BOOKING_CONFIRMED);

        return BookingResponse.from(saved);
    }

    // ─── Admin: tolak booking ─────────────────────────────────────────
    @Transactional
    public BookingResponse rejectBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking tidak ditemukan"));

        booking.setStatus(Booking.BookingStatus.REJECTED);
        Booking saved = bookingRepository.save(booking);

        sendNotification(booking.getUser(),
                "Booking Ditolak",
                "Booking lapangan " + booking.getCourt().getName() +
                        " pada " + booking.getBookingDate() + " ditolak. " +
                        "Silakan hubungi admin untuk informasi lebih lanjut.",
                Notification.NotificationType.BOOKING_REJECTED);

        return BookingResponse.from(saved);
    }

    // ─── Helper: cari booking dan validasi pemilik ────────────────────
    private Booking findBookingAndValidateOwner(Long bookingId, User user) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking dengan id " + bookingId + " tidak ditemukan"));

        // Admin boleh akses semua, user biasa hanya punya sendiri
        if (user.getRole() != User.Role.ADMIN &&
                !booking.getUser().getId().equals(user.getId())) {
            throw new BusinessException("Anda tidak punya akses ke booking ini");
        }

        return booking;
    }

    // ─── Helper: buat notifikasi ──────────────────────────────────────
    private void sendNotification(User user, String title, String message,
                                  Notification.NotificationType type) {
        Notification notif = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .build();
        notificationRepository.save(notif);
    }
}
