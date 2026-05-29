package com.kel6.booking.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Satu booking punya tepat satu payment record
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @Column(nullable = false)
    @Convert(converter = PaymentMethodConverter.class)
    private PaymentMethod method;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    // Path/URL file bukti pembayaran yang diupload user
    @Column(name = "proof_image_url", length = 500)
    private String proofImageUrl;

    @Column(nullable = false)
    @Convert(converter = PaymentStatusConverter.class)
    private PaymentStatus status = PaymentStatus.UNPAID;

    // Admin yang memverifikasi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by")
    private User verifiedBy;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ─── Enums ───────────────────────────────────────────────────────
    public enum PaymentMethod {
        TRANSFER, EWALLET, CASH
    }

    public enum PaymentStatus {
        UNPAID,     // belum ada aksi dari user
        UPLOADED,   // user sudah upload bukti, menunggu admin
        VERIFIED,   // admin sudah verifikasi
        FAILED      // verifikasi gagal / ditolak admin
    }

    @jakarta.persistence.Converter
    public static class PaymentStatusConverter
            implements jakarta.persistence.AttributeConverter<PaymentStatus, String> {

        @Override
        public String convertToDatabaseColumn(PaymentStatus paymentStatus) {
            return paymentStatus == null ? null : paymentStatus.name();
        }

        @Override
        public PaymentStatus convertToEntityAttribute(String value) {
            if (value == null) return null;
            return PaymentStatus.valueOf(value.toUpperCase());
        }
    }

    @jakarta.persistence.Converter
    public static class PaymentMethodConverter
            implements jakarta.persistence.AttributeConverter<PaymentMethod, String> {

        @Override
        public String convertToDatabaseColumn(PaymentMethod paymentMethod) {
            return paymentMethod == null ? null : paymentMethod.name();
        }

        @Override
        public PaymentMethod convertToEntityAttribute(String value) {
            if (value == null) return null;
            return PaymentMethod.valueOf(value.toUpperCase());
        }
    }
}
