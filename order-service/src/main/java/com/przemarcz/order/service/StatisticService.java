package com.przemarcz.order.service;

import com.przemarcz.order.dto.StatisticDto;
import com.przemarcz.order.model.Order;
import com.przemarcz.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.przemarcz.order.dto.StatisticDto.*;

@RequiredArgsConstructor
@Service
public class StatisticService {

    private final OrderRepository orderRepository;

    @Transactional(value = "transactionManager", readOnly = true)
    public void getRestaurantStatistic(UUID restaurantId, StatisticFilter filters) {
        List<Order> lastTenOrders = orderRepository.findTop10ByRestaurantIdOrderByTimeDesc(restaurantId);
        List<Order> ordersOfMonth = getOrdersOfMonth(restaurantId, Optional.ofNullable(filters.getDate()));
        List<Order> todayOrders = getOrdersOfToday(ordersOfMonth);
        System.out.println("ss");


        System.out.println("");
    }

    private List<Order> getOrdersOfToday(List<Order> ordersOfMonth) {
        int today = LocalDate.now().getDayOfMonth();
        return ordersOfMonth
                .stream()
                .filter(order -> order.getTime().getDayOfMonth()==today)
                .collect(Collectors.toList());
    }

    private List<Order> getOrdersOfMonth(UUID restaurantId, Optional<LocalDate> dateOptional) {
        LocalDate date = dateOptional.orElseGet(LocalDate::now);
        LocalDateTime startMonth = LocalDateTime.of(date.withDayOfMonth(1), LocalTime.MIN);
        LocalDateTime stopMonth = LocalDateTime.of(date.withDayOfMonth(date.lengthOfMonth()), LocalTime.MAX);
        return orderRepository.findAllByRestaurantIdAndTimeBetween(restaurantId, startMonth, stopMonth);
    }
}
