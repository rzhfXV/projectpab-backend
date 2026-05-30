package com.kel6.booking.DTO.response;

import com.kel6.booking.Model.Court;
import com.kel6.booking.Model.CourtSchedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    
    private Double rate;
    private Double latitude;
    private Double longitude;

    // Menampung data relasi halaman detail lapangan
    private List<ScheduleInfo> schedules;
    private List<CoachInfo> coaches;
    private List<EventInfo> events;
    private List<String> availableSlots;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ScheduleInfo {
        private Integer dayOfWeek;
        private String dayName;
        private LocalTime openTime;
        private LocalTime closeTime;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CoachInfo {
        private String name;
        private String availableTime;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class EventInfo {
        private String title;
        private String description;
    }

    public static CourtResponse from(Court court) {
        List<ScheduleInfo> scheduleInfos = new ArrayList<>();
        List<String> slots = new ArrayList<>();

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

            // LOGIKA DINAMIS: Ambil jadwal hari ini (atau default schedule pertama) untuk memecah slot jam
            if (!scheduleInfos.isEmpty()) {
                ScheduleInfo todaySchedule = scheduleInfos.get(0);
                slots = generateDynamicSlots(todaySchedule.getOpenTime(), todaySchedule.getCloseTime());
            }
        }

        // Jika data jadwal kosong di database, gunakan fallback slot standar agar FE tidak blank putih
        if (slots.isEmpty()) {
            slots = List.of("06:00", "07:30", "09:00", "10:30", "12:00", "13:30", "15:00", "16:30", "18:00", "19:30", "21:00");
        }

        // Nilai default awal koordinat & rate
        Double realRate = 4.5;
        Double realLat = -6.187738;
        Double realLng = 106.760154;
        
        List<CoachInfo> coachList = new ArrayList<>();
        List<EventInfo> eventList = new ArrayList<>();

        // VARIASI DATA TAMBAHAN UNTUK SETIAP COURT (Menyesuaikan ID Database kelompok)
        if (court.getId() != null) {
            int id = court.getId().intValue();
            switch (id) {
                case 1: // Futton Padel Club
                    realRate = 4.8; realLat = -6.242389; realLng = 106.753034;
                    coachList.add(new CoachInfo("Malika Shalshabila", "10.00 - 14.00"));
                    break;
                case 2: // Homeground Padel Kedoya
                    realRate = 4.5; realLat = -6.187738; realLng = 106.760154;
                    coachList.add(new CoachInfo("Coach Kenzie", "08.00 - 12.00"));
                    eventList.add(new EventInfo("Free Padel Coaching Clinic", "Latihan dasar padel gratis bersama pro player bagi pemula."));
                    break;
                case 3: // King Padel By Homeground
                    realRate = 4.9; realLat = -6.257201; realLng = 106.810135;
                    eventList.add(new EventInfo("Padel Weekend Cup 2026", "Turnamen ganda amatir berhadiah total Rp 10 Juta."));
                    break;
                case 4: // Orange Garden Padel Club
                    realRate = 4.7; realLat = -6.208114; realLng = 106.744801;
                    break;
                case 5: // Padelazo Kuningan
                    realRate = 4.6; realLat = -6.974664; realLng = 108.489212;
                    break;
            }
        }

        return CourtResponse.builder()
                .id(court.getId())
                .name(court.getName())
                .description(court.getDescription())
                .pricePerHour(court.getPricePerHour())
                .photoUrl(court.getPhotoUrl())
                .isActive(court.isActive())
                .schedules(scheduleInfos)
                .rate(realRate)
                .latitude(realLat)
                .longitude(realLng)
                .coaches(coachList)
                .events(eventList)
                .availableSlots(slots)
                .build();
    }

    // MEMPERBAIKI INDEKS HARI: SQL (1 = Minggu, 2 = Senin, dst)
    private static String getDayName(int day) {
        String[] names = {"", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};
        return (day >= 1 && day <= 7) ? names[day] : "Unknown";
    }

    // LOGIKA MATEMATIKA BE: Memecah jam buka s.d tutup menjadi potongan tombol per 1.5 jam secara dinamis!
    private static List<String> generateDynamicSlots(LocalTime openTime, LocalTime closeTime) {
        List<String> generatedSlots = new ArrayList<>();
        if (openTime == null || closeTime == null) return generatedSlots;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime current = openTime;

        // Batasi perulangan agar tidak melewati jam tutup operasional database
        while (current.plusMinutes(90).isBefore(closeTime) || current.plusMinutes(90).equals(closeTime) || closeTime.toString().startsWith("23:59")) {
            generatedSlots.add(current.format(formatter));
            current = current.plusMinutes(90); // Lompat per 1.5 jam (90 menit) sesuai kesepakatan UI/UX

            // Break jika waktu sudah mendekati tengah malam untuk menghindari infinite loop
            if (current.isBefore(openTime) || current.getHour() == 0) {
                break;
            }
        }
        return generatedSlots;
    }
}