package com.przemarcz.order.repository;

import com.przemarcz.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findAllByRestaurantId(@Param("restaurantId") UUID restaurantId, Pageable pageable);
    Optional<Order> findByRestaurantIdAndId(@Param("restaurantId") UUID restaurantId, @Param("id") UUID id);
}
