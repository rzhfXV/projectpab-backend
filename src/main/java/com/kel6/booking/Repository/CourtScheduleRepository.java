package com.kel6.booking.Repository;

import com.kel6.booking.Model.CourtSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourtScheduleRepository extends JpaRepository<CourtSchedule, Long> {

    // Ambil jadwal lapangan untuk hari tertentu
    // dayOfWeek: 0=Minggu, 1=Senin, ..., 6=Sabtu
    Optional<CourtSchedule> findByCourtIdAndDayOfWeekAndIsActiveTrue(
            Long courtId, Integer dayOfWeek);
}
