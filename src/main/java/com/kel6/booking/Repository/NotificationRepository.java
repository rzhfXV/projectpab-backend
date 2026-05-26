package com.kel6.booking.Repository;

import com.kel6.booking.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Semua notifikasi user, terbaru dulu
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Hanya yang belum dibaca
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);

    // Jumlah notifikasi belum dibaca (untuk badge di Android)
    long countByUserIdAndIsReadFalse(Long userId);
}
