package com.przemarcz.restaurant.repository;

import com.przemarcz.restaurant.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TableReservationRepository extends JpaRepository<Table, UUID> {
    List<Table> findAllByNumberOfSeatsAndReservationsDay(Integer numberOfSeats, LocalDate day);
}
