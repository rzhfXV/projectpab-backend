package com.kel6.booking.DTO.response;

import com.kel6.booking.Model.Court;
import com.kel6.booking.Model.CourtSchedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourtResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal pricePerHour;
    private String photoUrl;
    private boolean isActive;
    private List<ScheduleInfo> schedules;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleInfo {
        private Integer dayOfWeek; // 0=Minggu, 1=Senin, ..., 6=Sabtu
        private String dayName;
        private LocalTime openTime;
        private LocalTime closeTime;
    }

    public static CourtResponse from(Court court) {
        List<ScheduleInfo> scheduleInfos = null;
        if (court.getSchedules() != null) {
            scheduleInfos = court.getSchedules().stream()
                    .filter(CourtSchedule::isActive)
                    .map(s -> ScheduleInfo.builder()
                            .dayOfWeek(s.getDayOfWeek())
                            .dayName(getDayName(s.getDayOfWeek()))
                            .openTime(s.getOpenTime())
                            .closeTime(s.getCloseTime())
                            .build())
                    .collect(Collectors.toList());
        }
        return CourtResponse.builder()
                .id(court.getId())
                .name(court.getName())
                .description(court.getDescription())
                .pricePerHour(court.getPricePerHour())
                .photoUrl(court.getPhotoUrl())
                .isActive(court.isActive())
                .schedules(scheduleInfos)
                .build();
    }

    private static String getDayName(int day) {
        String[] names = {"Minggu","Senin","Selasa","Rabu","Kamis","Jumat","Sabtu"};
        return (day >= 0 && day <= 6) ? names[day] : "Unknown";
    }
}
