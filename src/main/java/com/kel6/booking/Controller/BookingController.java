package com.kel6.booking.Controller;

import com.kel6.booking.Config.SecurityHelper;
import com.kel6.booking.DTO.request.BookingRequest;
import com.kel6.booking.DTO.response.ApiResponse;
import com.kel6.booking.DTO.response.BookingResponse;
import com.kel6.booking.Model.User;
import com.kel6.booking.Service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final SecurityHelper securityHelper;

    /**
     * POST /api/bookings
     * Buat booking baru
     * Body: { courtId, bookingDate, startTime, endTime, notes }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @Valid @RequestBody BookingRequest request) {

        User currentUser = securityHelper.getCurrentUser();
        BookingResponse data = bookingService.createBooking(request, currentUser);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Booking berhasil dibuat", data));
    }

    /**
     * GET /api/bookings/my
     * Riwayat booking milik user yang sedang login
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings() {
        Long userId = securityHelper.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(
                bookingService.getMyBookings(userId)));
    }

    /**
     * GET /api/bookings/{id}
     * Detail satu booking — hanya pemilik atau admin
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(
            @PathVariable Long id) {

        User currentUser = securityHelper.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(
                bookingService.getBookingById(id, currentUser)));
    }

    /**
     * PATCH /api/bookings/{id}/cancel
     * User membatalkan booking sendiri
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(
            @PathVariable Long id) {

        User currentUser = securityHelper.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success("Booking berhasil dibatalkan",
                bookingService.cancelBooking(id, currentUser)));
    }

    // ─── Admin endpoints ──────────────────────────────────────────────

    /**
     * GET /api/admin/bookings?status=WAITING_PAYMENT
     * Admin: semua booking, bisa filter per status
     */
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings(
            @RequestParam(required = false) String status) {

        return ResponseEntity.ok(ApiResponse.success(
                bookingService.getAllBookings(status)));
    }

    /**
     * PATCH /api/bookings/admin/{id}/confirm
     * Admin: konfirmasi booking setelah verifikasi pembayaran
     */
    @PatchMapping("/admin/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BookingResponse>> confirmBooking(
            @PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success("Booking dikonfirmasi",
                bookingService.confirmBooking(id)));
    }

    /**
     * PATCH /api/bookings/admin/{id}/reject
     * Admin: tolak booking
     */
    @PatchMapping("/admin/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BookingResponse>> rejectBooking(
            @PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success("Booking ditolak",
                bookingService.rejectBooking(id)));
    }
}
