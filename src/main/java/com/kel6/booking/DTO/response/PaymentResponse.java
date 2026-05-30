package com.kel6.booking.DTO.response;

import com.kel6.booking.Model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long bookingId;
    private String method;
    private Integer amount; 
    private String proofImageUrl;
    private String status;
    private LocalDateTime verifiedAt;
    private String verifiedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String courtName; 

    public static PaymentResponse from(Payment payment) {
        if (payment == null) return null;

        String namaLapangan = "Lapangan Padel";
        if (payment.getBooking() != null && payment.getBooking().getCourt() != null) {
            namaLapangan = payment.getBooking().getCourt().getName();
        }

        Integer totalBayar = 0;
        if (payment.getAmount() != null) {
            totalBayar = payment.getAmount().intValue(); 
        }

        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(payment.getBooking() != null ? payment.getBooking().getId() : null)
                .method(payment.getMethod() != null ? payment.getMethod().name() : null)
                .amount(totalBayar)
                .proofImageUrl(payment.getProofImageUrl())
                .status(payment.getStatus() != null ? payment.getStatus().name() : null)
                .verifiedAt(payment.getVerifiedAt())
                .verifiedBy(payment.getVerifiedBy() != null ? payment.getVerifiedBy().getName() : null)
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .courtName(namaLapangan)
                .build();
    }
}