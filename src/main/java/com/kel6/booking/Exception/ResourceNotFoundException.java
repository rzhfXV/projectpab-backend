package com.kel6.booking.Exception;

// Dipakai ketika data tidak ditemukan di database
// Contoh: bookingRepository.findById(id) → kosong
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    // Contoh pemakaian: throw new ResourceNotFoundException("Booking dengan id " + id + " tidak ditemukan");
}
