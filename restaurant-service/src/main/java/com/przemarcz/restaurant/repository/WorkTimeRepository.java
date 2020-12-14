package com.przemarcz.restaurant.repository;

import com.przemarcz.restaurant.model.WorkTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkTimeRepository extends JpaRepository<WorkTime, UUID> {
    List<WorkTime> findAllByRestaurantId(@Param("restaurantId") UUID restaurantId);
}
