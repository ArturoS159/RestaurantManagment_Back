package com.przemarcz.order.service;

import com.przemarcz.order.model.Order;
import com.przemarcz.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StatisticService {

    private final OrderRepository orderRepository;

    @Transactional(value = "transactionManager", readOnly = true)
    public void getRestaurantStatisticForOwner(UUID restaurantId) {
//        List<Order> lastOrder
//        List<Order> orders = getOrdersOfMonth(UUID restaurantId)
//        LocalDate initial = LocalDate.now();
//        LocalDateTime start = LocalDateTime.of(initial.withDayOfMonth(1), LocalTime.MIN);
//        LocalDateTime stop = LocalDateTime.of(initial.withDayOfMonth(initial.lengthOfMonth()), LocalTime.MAX);
//        List<Order> orders = orderRepository.findAllByRestaurantIdAndTimeBetween(restaurantId, start, stop);
        System.out.println("");
    }
}
