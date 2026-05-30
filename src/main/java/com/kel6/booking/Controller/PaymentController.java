package com.kel6.booking.Controller;

import com.kel6.booking.DTO.response.ApiResponse;
import com.kel6.booking.DTO.response.PaymentResponse;
import com.kel6.booking.Model.User;
import com.kel6.booking.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getMyPaymentHistory(
            @AuthenticationPrincipal User user) {
        
        List<PaymentResponse> history = paymentService.getAllUserPayments(user);
        return ResponseEntity.ok(ApiResponse.success("Berhasil mengambil data riwayat pembayaran", history));
    }

    @PostMapping(value = "/{bookingId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PaymentResponse>> uploadProof(
            @PathVariable Long bookingId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user) throws IOException {
        
        PaymentResponse responseData = paymentService.uploadProof(bookingId, file, user);
        return ResponseEntity.ok(ApiResponse.success("Bukti pembayaran berhasil diunggah", responseData));
    }
}