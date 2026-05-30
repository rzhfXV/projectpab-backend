package com.kel6.booking.Controller;

import com.kel6.booking.DTO.response.CoachResponse;
import com.kel6.booking.Model.CoachSchedule;
import com.kel6.booking.Repository.CoachScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/coaches")
@CrossOrigin(origins = "*") 
public class CoachController {

    @Autowired
    private CoachScheduleRepository coachScheduleRepository;

    // URL: GET /api/coaches?date=2026-05-30
    @GetMapping
    public ResponseEntity<List<CoachResponse>> getCoachesByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        // 1. Langsung tarik data dari database lewat repository
        List<CoachSchedule> schedules = coachScheduleRepository.findAvailableSchedulesByDate(date);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm");
        
        // 2. Format langsung jadi JSON DTO Response yang dimintain Android
        List<CoachResponse> response = schedules.stream()
            .collect(Collectors.groupingBy(CoachSchedule::getCoach))
            .entrySet().stream()
            .map(entry -> {
                var coach = entry.getKey();
                var coachSchedules = entry.getValue();
                
                List<String> hours = coachSchedules.stream()
                    .map(s -> s.getStartTime().format(formatter))
                    .collect(Collectors.toList());
                
                return CoachResponse.builder()
                    .id(coach.getId())
                    .name(coach.getName())
                    .profileImageUrl(coach.getProfileImageUrl())
                    .availableHours(hours)
                    .build();
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}