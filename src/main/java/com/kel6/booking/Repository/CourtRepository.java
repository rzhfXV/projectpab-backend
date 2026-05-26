package com.kel6.booking.Repository;

import com.kel6.booking.Model.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {

    // Ambil semua lapangan yang aktif (untuk ditampilkan ke user)
    List<Court> findByIsActiveTrue();
}
