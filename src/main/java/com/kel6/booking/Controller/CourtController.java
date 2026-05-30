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

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourtResponse>>> getAllCourts() {
        return ResponseEntity.ok(ApiResponse.success(courtService.getAllActiveCourts()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourtResponse>> getCourtById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(courtService.getCourtById(id)));
    }

    @GetMapping("/{id}/slots")
    public ResponseEntity<ApiResponse<List<SlotResponse>>> getAvailableSlots(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.success(courtService.getAvailableSlots(id, date)));
    }

    @GetMapping("/near-me")
    public ResponseEntity<ApiResponse<List<CourtResponse>>> getNearMeCourts(
            @RequestParam("lat") Double latitude,
            @RequestParam("lng") Double longitude) {

        List<CourtResponse> allCourts = courtService.getAllActiveCourts();


        return ResponseEntity.ok(ApiResponse.success(allCourts));
    }
}