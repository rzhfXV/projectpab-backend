package com.kel6.booking.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull(message = "Metode pembayaran tidak boleh kosong")
    private String method; // TRANSFER, EWALLET, CASH
}
