package com.przemarcz.restaurant.repository;

import com.przemarcz.restaurant.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    Page<Reservation> findAllByDay(LocalDate day, Pageable pageable);

    Page<Reservation> findAllByUserId(UUID userId, Pageable pageable);
}
