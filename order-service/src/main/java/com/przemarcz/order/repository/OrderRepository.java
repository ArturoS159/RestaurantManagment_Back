package com.przemarcz.order.repository;

import com.przemarcz.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    Page<Order> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Order> findByRestaurantIdAndId(UUID restaurantId, UUID id);
}
