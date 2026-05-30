package com.kel6.booking.Repository;

import com.kel6.booking.Model.CoachSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CoachScheduleRepository extends JpaRepository<CoachSchedule, Long> {

    // Query sakti untuk mengambil semua jadwal pelatih yang tersedia di tanggal tertentu
    // JOIN FETCH digunakan supaya data objek Coach di dalamnya langsung ikut terbawa tanpa Lemot (LazyInitializationException)
    @Query("SELECT cs FROM CoachSchedule cs JOIN FETCH cs.coach WHERE cs.sessionDate = :date AND cs.isBooked = 0")
    List<CoachSchedule> findAvailableSchedulesByDate(@Param("date") LocalDate date);
}