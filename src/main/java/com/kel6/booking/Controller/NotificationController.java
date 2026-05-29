package com.kel6.booking.Controller;

import com.kel6.booking.Config.SecurityHelper;
import com.kel6.booking.DTO.response.ApiResponse;
import com.kel6.booking.Model.Notification;
import com.kel6.booking.Repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final SecurityHelper securityHelper;

    /**
     * GET /api/notifications
     * Semua notifikasi user yang login, terbaru dulu
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Notification>>> getAll() {
        Long userId = securityHelper.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(
                notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)));
    }

    /**
     * GET /api/notifications/unread-count
     * Jumlah notifikasi belum dibaca (untuk badge di Android)
     */
    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getUnreadCount() {
        Long userId = securityHelper.getCurrentUserId();
        long count = notificationRepository.countByUserIdAndIsReadFalse(userId);
        return ResponseEntity.ok(ApiResponse.success(Map.of("count", count)));
    }

    /**
     * PATCH /api/notifications/{id}/read
     * Tandai satu notifikasi sebagai sudah dibaca
     */
    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long id) {
        Long userId = securityHelper.getCurrentUserId();
        notificationRepository.findById(id).ifPresent(notif -> {
            if (notif.getUser().getId().equals(userId)) {
                notif.setRead(true);
                notificationRepository.save(notif);
            }
        });
        return ResponseEntity.ok(ApiResponse.success("Notifikasi ditandai dibaca", null));
    }

    /**
     * PATCH /api/notifications/read-all
     * Tandai semua notifikasi user sebagai sudah dibaca
     */
    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead() {
        Long userId = securityHelper.getCurrentUserId();
        List<Notification> unread = notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
        return ResponseEntity.ok(ApiResponse.success(
                unread.size() + " notifikasi ditandai dibaca", null));
    }
}
