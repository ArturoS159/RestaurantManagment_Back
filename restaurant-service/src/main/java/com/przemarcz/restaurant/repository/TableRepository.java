package com.przemarcz.restaurant.repository;

import com.przemarcz.restaurant.model.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TableRepository extends JpaRepository<Table, UUID> {
    Optional<Table> findByIdAndRestaurantId(UUID id, UUID restaurantId);
}
