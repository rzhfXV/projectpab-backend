package com.kel6.booking.Controller;

import com.kel6.booking.DTO.response.ApiResponse;
import com.kel6.booking.DTO.response.CourtResponse;
import com.kel6.booking.DTO.response.SlotResponse;
import com.kel6.booking.Service.CourtService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/courts")
@RequiredArgsConstructor
public class CourtController {

    private final CourtService courtService;

    /**
     * GET /api/courts
     * Publik — tidak butuh token
     * Kembalikan semua lapangan aktif
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CourtResponse>>> getAllCourts() {
        return ResponseEntity.ok(ApiResponse.success(courtService.getAllActiveCourts()));
    }

    /**
     * GET /api/courts/{id}
     * Publik — detail satu lapangan beserta jadwal operasional
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourtResponse>> getCourtById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(courtService.getCourtById(id)));
    }

    /**
     * GET /api/courts/{id}/slots?date=2025-07-01
     * Publik — daftar slot 1 jam + status available/taken
     * Android pakai ini untuk tampilkan kalender booking
     */
    @GetMapping("/{id}/slots")
    public ResponseEntity<ApiResponse<List<SlotResponse>>> getAvailableSlots(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return ResponseEntity.ok(ApiResponse.success(
                courtService.getAvailableSlots(id, date)));
    }
}
