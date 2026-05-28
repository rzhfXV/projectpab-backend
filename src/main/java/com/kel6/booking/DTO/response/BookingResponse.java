package com.kel6.booking.DTO.response;

import com.kel6.booking.Model.Booking;
import com.kel6.booking.Model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private Long id;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal durationHours;
    private BigDecimal totalPrice;
    private String status;
    private String notes;
    private String qrToken;
    private LocalDateTime createdAt;

    private Long courtId;
    private String courtName;
    private String courtPhotoUrl;

    private String paymentMethod;
    private String paymentStatus;
    private String proofImageUrl;

    public static BookingResponse from(Booking booking) {
        BookingResponse res = BookingResponse.builder()
                .id(booking.getId())
                .bookingDate(booking.getBookingDate())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .durationHours(booking.getDurationHours())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus().name())
                .notes(booking.getNotes())
                .qrToken(booking.getQrToken())
                .createdAt(booking.getCreatedAt())
                .courtId(booking.getCourt().getId())
                .courtName(booking.getCourt().getName())
                .courtPhotoUrl(booking.getCourt().getPhotoUrl())
                .build();

        if (booking.getPayment() != null) {
            Payment p = booking.getPayment();
            res.setPaymentMethod(p.getMethod().name());
            res.setPaymentStatus(p.getStatus().name());
            res.setProofImageUrl(p.getProofImageUrl());
        }

        return res;
    }
}
