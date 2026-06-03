package com.kel6.booking.Service;

import com.kel6.booking.DTO.response.CourtResponse;
import com.kel6.booking.DTO.response.SlotResponse;
import com.kel6.booking.Exception.BusinessException;
import com.kel6.booking.Exception.ResourceNotFoundException;
import com.kel6.booking.Model.Booking;
import com.kel6.booking.Model.Court;
import com.kel6.booking.Model.CourtSchedule;
import com.kel6.booking.Repository.BookingRepository;
import com.kel6.booking.Repository.CourtRepository;
import com.kel6.booking.Repository.CourtScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourtService {

    private final CourtRepository courtRepository;
    private final CourtScheduleRepository courtScheduleRepository;
    private final BookingRepository bookingRepository;

    // Semua lapangan aktif
    public List<CourtResponse> getAllActiveCourts() {
        return courtRepository.findByIsActiveTrue()
                .stream()
                .map(CourtResponse::from)
                .collect(Collectors.toList());
    }

    // Detail satu lapangan
    public CourtResponse getCourtById(Long id) {
        Court court = courtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lapangan dengan id " + id + " tidak ditemukan"));
        return CourtResponse.from(court);
    }

    // Slot tersedia di tanggal tertentu (per 1 jam)
    public List<SlotResponse> getAvailableSlots(Long courtId, LocalDate date) {

        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lapangan dengan id " + courtId + " tidak ditemukan"));

        if (!court.isActive()) {
            throw new BusinessException("Lapangan ini sedang tidak aktif");
        }

        // Konversi: Java DayOfWeek 1(Mon)..7(Sun) → kita 0(Sun)..6(Sat)
        int javaDayValue = date.getDayOfWeek().getValue();
        int ourDayOfWeek = (javaDayValue % 7);

        CourtSchedule schedule = courtScheduleRepository
                .findByCourtIdAndDayOfWeekAndIsActiveTrue(courtId, ourDayOfWeek)
                .orElseThrow(() -> new BusinessException(
                        "Lapangan tidak beroperasi pada hari tersebut"));

        List<Booking> existingBookings = bookingRepository
                .findActiveBookingsByCourtAndDate(courtId, date);

        // Generate slot per 1 jam
        List<SlotResponse> slots = new ArrayList<>();
        LocalTime cursor = schedule.getOpenTime();

        while (cursor.plusHours(1).compareTo(schedule.getCloseTime()) <= 0) {
            LocalTime slotStart = cursor;
            LocalTime slotEnd = cursor.plusHours(1);

            boolean isTaken = existingBookings.stream().anyMatch(b ->
                    b.getStartTime().isBefore(slotEnd) &&
                            b.getEndTime().isAfter(slotStart)
            );

            slots.add(SlotResponse.builder()
                    .startTime(slotStart)
                    .endTime(slotEnd)
                    .available(!isTaken)
                    .build());

            cursor = cursor.plusHours(1);
        }

        return slots;
    }
}
