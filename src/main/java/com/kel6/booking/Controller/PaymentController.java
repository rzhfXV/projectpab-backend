package com.kel6.booking.Controller;

import com.kel6.booking.Config.SecurityHelper;
import com.kel6.booking.DTO.request.PaymentRequest;
import com.kel6.booking.DTO.response.ApiResponse;
import com.kel6.booking.DTO.response.PaymentResponse;
import com.kel6.booking.Model.User;
import com.kel6.booking.Service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final SecurityHelper securityHelper;

    /**
     * POST /api/payments/{bookingId}/method
     * Step 1: User pilih metode pembayaran
     * Body: { "method": "TRANSFER" }
     */
    @PostMapping("/{bookingId}/method")
    public ResponseEntity<ApiResponse<PaymentResponse>> selectMethod(
            @PathVariable Long bookingId,
            @Valid @RequestBody PaymentRequest request) {

        User user = securityHelper.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(
                "Metode pembayaran dipilih",
                paymentService.selectPaymentMethod(bookingId, request, user)));
    }

    /**
     * POST /api/payments/{bookingId}/proof
     * Step 2: User upload bukti pembayaran
     * Content-Type: multipart/form-data
     * Form field: "file" (gambar JPG/PNG)
     */
    @PostMapping(value = "/{bookingId}/proof",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PaymentResponse>> uploadProof(
            @PathVariable Long bookingId,
            @RequestParam("file") MultipartFile file) throws IOException {

        User user = securityHelper.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(
                "Bukti pembayaran berhasil diupload",
                paymentService.uploadProof(bookingId, file, user)));
    }

    /**
     * GET /api/payments/{bookingId}
     * Lihat status payment booking
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(
            @PathVariable Long bookingId) {

        User user = securityHelper.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(
                paymentService.getPaymentByBookingId(bookingId, user)));
    }

    /**
     * PATCH /api/payments/admin/{bookingId}/verify?approved=true
     * Admin: verifikasi atau tolak bukti pembayaran
     */
    @PatchMapping("/admin/{bookingId}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaymentResponse>> verifyPayment(
            @PathVariable Long bookingId,
            @RequestParam boolean approved) throws IOException {

        User admin = securityHelper.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(
                approved ? "Pembayaran diverifikasi" : "Pembayaran ditolak",
                paymentService.verifyPayment(bookingId, approved, admin)));
    }
}
