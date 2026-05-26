package com.kel6.booking.Repository;

import com.kel6.booking.Model.Booking;
import com.kel6.booking.Model.Booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // ── Riwayat booking milik user tertentu ──────────────────────────
    List<Booking> findByUserIdOrderByBookingDateDescStartTimeDesc(Long userId);

    // ── Cek konflik jadwal sebelum booking dibuat ────────────────────
    // Logika: ada booking aktif di court + tanggal yang sama,
    // dan waktunya overlap dengan slot yang diminta
    @Query("""
        SELECT COUNT(b) FROM Booking b
        WHERE b.court.id   = :courtId
          AND b.bookingDate = :bookingDate
          AND b.status NOT IN ('CANCELLED', 'REJECTED')
          AND b.startTime  < :endTime
          AND b.endTime    > :startTime
    """)
    long countConflictingBookings(
            @Param("courtId")     Long courtId,
            @Param("bookingDate") LocalDate bookingDate,
            @Param("startTime")   LocalTime startTime,
            @Param("endTime")     LocalTime endTime
    );

    // ── Ambil semua booking aktif di lapangan pada tanggal tertentu ──
    // Dipakai untuk menampilkan slot mana saja yang sudah terisi
    @Query("""
        SELECT b FROM Booking b
        WHERE b.court.id   = :courtId
          AND b.bookingDate = :bookingDate
          AND b.status NOT IN ('CANCELLED', 'REJECTED')
        ORDER BY b.startTime
    """)
    List<Booking> findActiveBookingsByCourtAndDate(
            @Param("courtId")     Long courtId,
            @Param("bookingDate") LocalDate bookingDate
    );

    // ── Admin: ambil booking berdasarkan status ───────────────────────
    List<Booking> findByStatusOrderByCreatedAtAsc(BookingStatus status);

    // ── Booking by user + status tertentu ────────────────────────────
    List<Booking> findByUserIdAndStatusOrderByBookingDateDesc(Long userId, BookingStatus status);
}
