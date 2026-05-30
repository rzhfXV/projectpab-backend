package com.kel6.booking.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coach_schedules")
public class CoachSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate; // Membaca tipe DATE (Tahun-Bulan-Hari)

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;   // Membaca tipe TIME (Jam:Menit)

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;     // Membaca tipe TIME (Jam:Menit)

    @Column(name = "is_booked")
    private Integer isBooked = 0;  // 0 jika belum laku, 1 jika sudah laku
}