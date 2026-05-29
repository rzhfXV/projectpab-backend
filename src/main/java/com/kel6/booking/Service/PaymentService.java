package com.kel6.booking.Service;

import com.kel6.booking.DTO.request.PaymentRequest;
import com.kel6.booking.DTO.response.PaymentResponse;
import com.kel6.booking.Exception.BusinessException;
import com.kel6.booking.Exception.ResourceNotFoundException;
import com.kel6.booking.Model.*;
import com.kel6.booking.Repository.BookingRepository;
import com.kel6.booking.Repository.NotificationRepository;
import com.kel6.booking.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final NotificationRepository notificationRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    // ─── Step 1: User pilih metode pembayaran ─────────────────────────
    // Dipanggil setelah booking dibuat (status PENDING)
    @Transactional
    public PaymentResponse selectPaymentMethod(Long bookingId,
                                               PaymentRequest request,
                                               User user) {
        Booking booking = getBookingAndValidateOwner(bookingId, user);

        if (booking.getStatus() != Booking.BookingStatus.PENDING) {
            throw new BusinessException(
                    "Pembayaran hanya bisa dilakukan untuk booking berstatus PENDING");
        }

        // Cek kalau sudah ada payment record sebelumnya
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElse(null);

        if (payment == null) {
            // Buat payment record baru
            Payment.PaymentMethod method;
            try {
                method = Payment.PaymentMethod.valueOf(request.getMethod().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("Metode pembayaran tidak valid: " + request.getMethod() +
                        ". Pilih: TRANSFER, EWALLET, atau CASH");
            }

            payment = Payment.builder()
                    .booking(booking)
                    .method(method)
                    .amount(booking.getTotalPrice())
                    .status(Payment.PaymentStatus.UNPAID)
                    .build();

            paymentRepository.save(payment);
        }

        // Update status booking jadi WAITING_PAYMENT
        booking.setStatus(Booking.BookingStatus.WAITING_PAYMENT);
        bookingRepository.save(booking);

        return PaymentResponse.from(payment);
    }

    // ─── Step 2: User upload bukti pembayaran ─────────────────────────
    @Transactional
    public PaymentResponse uploadProof(Long bookingId,
                                       MultipartFile file,
                                       User user) throws IOException {
        Booking booking = getBookingAndValidateOwner(bookingId, user);

        if (booking.getStatus() != Booking.BookingStatus.WAITING_PAYMENT) {
            throw new BusinessException(
                    "Upload bukti hanya bisa untuk booking berstatus WAITING_PAYMENT");
        }

        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new BusinessException(
                        "Pilih metode pembayaran terlebih dahulu"));

        // Validasi file
        validateImageFile(file);

        // Simpan file ke disk
        String filename = saveFile(file, bookingId);

        // Update payment record
        payment.setProofImageUrl(filename);
        payment.setStatus(Payment.PaymentStatus.UPLOADED);
        paymentRepository.save(payment);

        // Notifikasi user
        sendNotification(user,
                "Bukti Bayar Diterima",
                "Bukti pembayaran booking #" + bookingId +
                        " sudah kami terima. Menunggu verifikasi admin.",
                Notification.NotificationType.PAYMENT_VERIFIED);

        return PaymentResponse.from(payment);
    }

    // ─── Step 3 (Admin): Verifikasi bukti pembayaran ──────────────────
    @Transactional
    public PaymentResponse verifyPayment(Long bookingId, boolean approved, User admin) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking tidak ditemukan"));

        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Data payment tidak ditemukan"));

        if (payment.getStatus() != Payment.PaymentStatus.UPLOADED) {
            throw new BusinessException("Payment belum ada bukti yang diupload");
        }

        if (approved) {
            payment.setStatus(Payment.PaymentStatus.VERIFIED);
            payment.setVerifiedBy(admin);
            payment.setVerifiedAt(LocalDateTime.now());
            booking.setStatus(Booking.BookingStatus.CONFIRMED);

            sendNotification(booking.getUser(),
                    "Pembayaran Terverifikasi!",
                    "Pembayaran booking lapangan " + booking.getCourt().getName() +
                            " tanggal " + booking.getBookingDate() + " telah diverifikasi. " +
                            "Sampai jumpa di lapangan!",
                    Notification.NotificationType.PAYMENT_VERIFIED);
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            booking.setStatus(Booking.BookingStatus.REJECTED);

            sendNotification(booking.getUser(),
                    "Pembayaran Ditolak",
                    "Bukti pembayaran booking #" + bookingId +
                            " ditolak. Silakan hubungi admin.",
                    Notification.NotificationType.BOOKING_REJECTED);
        }

        paymentRepository.save(payment);
        bookingRepository.save(booking);

        return PaymentResponse.from(payment);
    }

    // ─── Get payment by booking id ────────────────────────────────────
    public PaymentResponse getPaymentByBookingId(Long bookingId, User user) {
        getBookingAndValidateOwner(bookingId, user);

        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Data payment untuk booking #" + bookingId + " belum ada"));

        return PaymentResponse.from(payment);
    }

    // ─── Helper: validasi file gambar ─────────────────────────────────
    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("File tidak boleh kosong");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/jpeg") &&
                        !contentType.equals("image/png") &&
                        !contentType.equals("image/jpg"))) {
            throw new BusinessException("Format file harus JPG atau PNG");
        }

        // Maksimal 5MB
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BusinessException("Ukuran file maksimal 5MB");
        }
    }

    // ─── Helper: simpan file ke disk ──────────────────────────────────
    private String saveFile(MultipartFile file, Long bookingId) throws IOException {
        // Buat folder kalau belum ada
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate nama file unik: booking_{id}_{uuid}.jpg
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = "booking_" + bookingId + "_" + UUID.randomUUID() + extension;

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filename; // simpan nama file saja, bukan full path
    }

    // ─── Helper: validasi owner ───────────────────────────────────────
    private Booking getBookingAndValidateOwner(Long bookingId, User user) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking #" + bookingId + " tidak ditemukan"));

        if (user.getRole() != User.Role.ADMIN &&
                !booking.getUser().getId().equals(user.getId())) {
            throw new BusinessException("Anda tidak punya akses ke booking ini");
        }
        return booking;
    }

    // ─── Helper: kirim notifikasi ─────────────────────────────────────
    private void sendNotification(User user, String title, String message,
                                  Notification.NotificationType type) {
        notificationRepository.save(Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .build());
    }
}
