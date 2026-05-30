package com.kel6.booking.Repository;

import com.kel6.booking.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBookingId(Long bookingId);
    
    // Tambahkan ini untuk mengambil daftar riwayat pembayaran berdasarkan ID User
    List<Payment> findAllByBookingUserIdOrderByCreatedAtDesc(Long userId);
}