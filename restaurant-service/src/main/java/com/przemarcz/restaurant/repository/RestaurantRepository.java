package com.przemarcz.restaurant.repository;

import com.przemarcz.restaurant.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID>, JpaSpecificationExecutor<Restaurant> {
    Page<Restaurant> findAllByIdIn(List<UUID> restaurants, Pageable pageable);

    Optional<Restaurant> findByIdAndIsDeleted(@Param("id") UUID id, @Param("isDeleted") boolean isDeleted);
}
