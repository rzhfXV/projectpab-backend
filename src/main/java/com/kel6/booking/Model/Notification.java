package com.kel6.booking.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    @Convert(converter = NotificationTypeConverter.class)
    private NotificationType type;

    @Column(name = "is_read", nullable = false)
    @JsonProperty("read")
    private boolean isRead = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // ─── Enum Type ────────────────────────────────────────────────────
    public enum NotificationType {
        BOOKING_CONFIRMED,
        BOOKING_REJECTED,
        BOOKING_CANCELLED,
        PAYMENT_VERIFIED,
        REMINDER
    }

    @jakarta.persistence.Converter
    public static class NotificationTypeConverter
            implements jakarta.persistence.AttributeConverter<NotificationType, String> {

        @Override
        public String convertToDatabaseColumn(NotificationType notificationType) {
            return notificationType == null ? null : notificationType.name(); // simpan sebagai "USER" / "ADMIN"
        }

        @Override
        public NotificationType convertToEntityAttribute(String value) {
            if (value == null) return null;
            return NotificationType.valueOf(value.toUpperCase()); // baca "user" → USER
        }
    }
}
