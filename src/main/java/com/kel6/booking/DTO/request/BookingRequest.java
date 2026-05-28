package com.kel6.booking.DTO.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingRequest {

    @NotNull(message = "ID lapangan tidak boleh kosong")
    private Long courtId;

    @NotNull(message = "Tanggal booking tidak boleh kosong")
    @Future(message = "Tanggal booking harus di masa depan")
    private LocalDate bookingDate;

    @NotNull(message = "Jam mulai tidak boleh kosong")
    private LocalTime startTime;

    @NotNull(message = "Jam selesai tidak boleh kosong")
    private LocalTime endTime;

    private String notes;
}
