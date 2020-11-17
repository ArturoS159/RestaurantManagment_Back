package com.przemarcz.order.repository;

import com.przemarcz.order.model.RestaurantPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<RestaurantPayment, UUID> {
}
