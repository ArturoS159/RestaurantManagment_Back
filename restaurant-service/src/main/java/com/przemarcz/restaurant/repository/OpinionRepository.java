package com.przemarcz.restaurant.repository;

import com.przemarcz.restaurant.model.Opinion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, UUID> {
    Page<Opinion> findAllByRestaurantId(@Param("restaurantId") UUID restaurantId, Pageable pageable);
}
