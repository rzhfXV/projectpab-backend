package com.kel6.booking.Exception;

// Dipakai ketika logika bisnis tidak bisa dijalankan
// Contoh: slot sudah dibooking orang lain, lapangan tidak aktif
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
    // Contoh pemakaian: throw new BusinessException("Slot waktu ini sudah dipesan");
}
