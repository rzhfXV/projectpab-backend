package com.kel6.booking.DTO.response;

import com.kel6.booking.Model.Payment;
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
public class PaymentResponse {

    private Long id;
    private Long bookingId;
    private String method;
    private BigDecimal amount;
    private String proofImageUrl;
    private String status;
    private LocalDateTime verifiedAt;
    private String verifiedBy; // nama admin

    public static PaymentResponse from(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(payment.getBooking().getId())
                .method(payment.getMethod().name())
                .amount(payment.getAmount())
                .proofImageUrl(payment.getProofImageUrl())
                .status(payment.getStatus().name())
                .verifiedAt(payment.getVerifiedAt())
                .verifiedBy(payment.getVerifiedBy() != null
                        ? payment.getVerifiedBy().getName() : null)
                .build();
    }
}
