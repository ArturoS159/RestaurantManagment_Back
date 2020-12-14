package com.przemarcz.restaurant.repository;

import com.przemarcz.restaurant.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MealRepository extends JpaRepository<Meal, UUID>, JpaSpecificationExecutor<Meal> {
    Optional<Meal> findByIdAndRestaurantId(@Param("id") UUID id, @Param("restaurantId") UUID restaurantId);
}
