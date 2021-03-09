package com.przemarcz.order.service;

import com.przemarcz.order.mapper.OrderMapper;
import com.przemarcz.order.model.Order;
import com.przemarcz.order.model.TotalStatistic;
import com.przemarcz.order.repository.OrderRepository;
import com.przemarcz.order.repository.TotalStatisticRepository;
import com.przemarcz.order.util.StatisticHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.przemarcz.order.dto.OrderDto.OrderStatisticResponse;
import static com.przemarcz.order.dto.StatisticDto.*;

@RequiredArgsConstructor
@Service
public class StatisticService {

    private static final long ZERO = 0L;
    private final StatisticHelper statisticHelper;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final TotalStatisticRepository totalStatisticRepository;

    @Transactional(value = "transactionManager", readOnly = true)
    public StatisticResponse getRestaurantStatistic(UUID restaurantId, StatisticFilter filters) throws ExecutionException, InterruptedException {
        List<OrderStatisticResponse> lastTenOrders = orderRepository.findTop10ByRestaurantIdOrderByTimeDesc(restaurantId)
                .stream().map(orderMapper::toOrderStatisticResponse).collect(Collectors.toList());
        Optional<TotalStatistic> totalStatistic = totalStatisticRepository.findById(restaurantId);
        Set<Order> todayOrders = getOrdersOfToday(restaurantId);
        Set<Order> ordersOfMonth = getOrdersOfMonth(restaurantId, filters.getDate());

        Future<Set<ProfitResponse>> restaurantProfit = statisticHelper.getRestaurantProfit(todayOrders, ordersOfMonth, totalStatistic);
        Future<Set<CountOrderResponse>> ordersCount = statisticHelper.getOrdersCount(todayOrders, ordersOfMonth, totalStatistic);
        Future<Set<TypedCountOrderResponse>> ordersTypedCount = statisticHelper.getOrdersTypedCount(todayOrders, ordersOfMonth, totalStatistic);
        Future<Set<TypedPaymentOrderResponse>> paymentMethodCount = statisticHelper.getOrdersTypedPaymentMethodCount(todayOrders, ordersOfMonth, totalStatistic);
        Future<Set<PopularMealsResponse>> popularMeals = statisticHelper.getPopularMeals(todayOrders, ordersOfMonth, totalStatistic);
        return StatisticResponse.builder()
                .lastOrders(lastTenOrders)
                .profit(restaurantProfit.get())
                .counts(ordersCount.get())
                .typedCounts(ordersTypedCount.get())
                .paymentCount(paymentMethodCount.get())
                .popularMeals(popularMeals.get())
                .build();
    }

    private Set<Order> getOrdersOfToday(UUID restaurantId) {
        LocalDate todayDate = LocalDate.now();
        LocalDateTime startDateOfToday = todayDate.atTime(LocalTime.MIN);
        LocalDateTime stopDateOfToday = todayDate.atTime(LocalTime.MAX);
        return orderRepository.findAllByRestaurantIdAndTimeBetween(restaurantId, startDateOfToday, stopDateOfToday);
    }

    private Set<Order> getOrdersOfMonth(UUID restaurantId, LocalDate date) {
        LocalDate localDate = Optional.ofNullable(date).orElseGet(LocalDate::now);
        LocalDateTime startDateOfMonth = LocalDateTime.of(localDate.withDayOfMonth(1), LocalTime.MIN);
        LocalDateTime stopDateOfMonth = LocalDateTime.of(localDate.withDayOfMonth(localDate.lengthOfMonth()), LocalTime.MAX);
        return orderRepository.findAllByRestaurantIdAndTimeBetween(restaurantId, startDateOfMonth, stopDateOfMonth);
    }

    public void addStatistic(Order order) {
        TotalStatistic statistic = totalStatisticRepository.findById(order.getRestaurantId())
                .orElse(TotalStatistic.builder()
                        .id(order.getRestaurantId())
                        .totalProfit(BigDecimal.ZERO)
                        .totalOrders(ZERO)
                        .totalComments(ZERO)
                        .totalPaymentCash(ZERO)
                        .totalPaymentOnline(ZERO)
                        .totalOrderInLocal(ZERO)
                        .totalOrderTakeAway(ZERO)
                        .totalOrderDelivery(ZERO)
                        .build());
        statistic.addStatistics(order);
        totalStatisticRepository.save(statistic);
    }

}
