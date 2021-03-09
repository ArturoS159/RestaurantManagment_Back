package com.przemarcz.order.util;

import com.przemarcz.order.model.Order;
import com.przemarcz.order.model.PopularMealsStatistic;
import com.przemarcz.order.model.TotalStatistic;
import com.przemarcz.order.model.enums.OrderType;
import com.przemarcz.order.model.enums.PaymentMethod;
import com.przemarcz.order.model.enums.TimeSpan;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.przemarcz.order.dto.StatisticDto.*;

@Component
public class StatisticHelper {

    private static final long ZERO = 0L;

    @Async
    public Future<Set<ProfitResponse>> getRestaurantProfit(Set<Order> todayOrders, Set<Order> ordersOfMonth, Optional<TotalStatistic> totalStatistic) {
        Set<ProfitResponse> restaurantProfit = new HashSet<>();
        restaurantProfit.add(new ProfitResponse(TimeSpan.TODAY, countRestaurantProfit(todayOrders)));
        restaurantProfit.add(new ProfitResponse(TimeSpan.MONTH, countRestaurantProfit(ordersOfMonth)));
        restaurantProfit.add(new ProfitResponse(TimeSpan.TOTAL, totalStatistic.map(TotalStatistic::getTotalProfit).orElse(BigDecimal.ZERO)));
        return new AsyncResult<>(restaurantProfit);
    }

    private BigDecimal countRestaurantProfit(Set<Order> orders) {
        return orders.stream()
                .map(Order::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Async
    public Future<Set<CountOrderResponse>> getOrdersCount(Set<Order> todayOrders, Set<Order> ordersOfMonth, Optional<TotalStatistic> totalStatistic) {
        Set<CountOrderResponse> ordersCount = new HashSet<>();
        ordersCount.add(new CountOrderResponse(TimeSpan.TODAY, todayOrders.size()));
        ordersCount.add(new CountOrderResponse(TimeSpan.MONTH, ordersOfMonth.size()));
        ordersCount.add(new CountOrderResponse(TimeSpan.TOTAL, totalStatistic.map(TotalStatistic::getTotalOrders).orElse(ZERO)));
        return new AsyncResult<>(ordersCount);
    }

    @Async
    public Future<Set<TypedCountOrderResponse>> getOrdersTypedCount(Set<Order> todayOrders, Set<Order> ordersOfMonth, Optional<TotalStatistic> totalStatistic) {
        Set<TypedCountOrderResponse> ordersTypedCount = new HashSet<>();
        ordersTypedCount.addAll(getOrdersTypedCountByTime(TimeSpan.TODAY, todayOrders));
        ordersTypedCount.addAll(getOrdersTypedCountByTime(TimeSpan.MONTH, ordersOfMonth));
        ordersTypedCount.add(new TypedCountOrderResponse(TimeSpan.TOTAL, OrderType.DELIVERY, totalStatistic.map(TotalStatistic::getTotalOrderDelivery).orElse(ZERO)));
        ordersTypedCount.add(new TypedCountOrderResponse(TimeSpan.TOTAL, OrderType.IN_LOCAL, totalStatistic.map(TotalStatistic::getTotalOrderInLocal).orElse(ZERO)));
        ordersTypedCount.add(new TypedCountOrderResponse(TimeSpan.TOTAL, OrderType.TAKE_AWAY, totalStatistic.map(TotalStatistic::getTotalOrderTakeAway).orElse(ZERO)));
        return new AsyncResult<>(ordersTypedCount);
    }

    private Set<TypedCountOrderResponse> getOrdersTypedCountByTime(TimeSpan timeSpan, Set<Order> orders) {
        return Arrays.stream(OrderType.values())
                .map(orderType -> new TypedCountOrderResponse(timeSpan, orderType, calculateCountTypedOrder(orders, orderType)))
                .collect(Collectors.toSet());
    }

    private long calculateCountTypedOrder(Set<Order> orders, OrderType orderType) {
        return orders.stream().filter(order -> order.getOrderType().equals(orderType)).count();
    }

    @Async
    public Future<Set<TypedPaymentOrderResponse>> getOrdersTypedPaymentMethodCount(Set<Order> todayOrders, Set<Order> ordersOfMonth, Optional<TotalStatistic> totalStatistic) {
        Set<TypedPaymentOrderResponse> paymentMethodCount = new HashSet<>();
        paymentMethodCount.addAll(getPaymentMethodCount(TimeSpan.TODAY, todayOrders));
        paymentMethodCount.addAll(getPaymentMethodCount(TimeSpan.MONTH, ordersOfMonth));
        paymentMethodCount.add(new TypedPaymentOrderResponse(TimeSpan.TOTAL, PaymentMethod.ONLINE, totalStatistic.map(TotalStatistic::getTotalPaymentOnline).orElse(ZERO)));
        paymentMethodCount.add(new TypedPaymentOrderResponse(TimeSpan.TOTAL, PaymentMethod.CASH, totalStatistic.map(TotalStatistic::getTotalPaymentCash).orElse(ZERO)));
        return new AsyncResult<>(paymentMethodCount);
    }

    private Set<TypedPaymentOrderResponse> getPaymentMethodCount(TimeSpan timeSpan, Set<Order> orders) {
        return Arrays.stream(PaymentMethod.values())
                .map(paymentMethod -> new TypedPaymentOrderResponse(timeSpan, paymentMethod, calculateCountTypePaymentMethod(orders, paymentMethod)))
                .collect(Collectors.toSet());
    }

    private long calculateCountTypePaymentMethod(Set<Order> orders, PaymentMethod paymentMethod) {
        return orders.stream().filter(order -> order.getPaymentMethod().equals(paymentMethod)).count();
    }

    @Async
    public Future<Set<PopularMealsResponse>> getPopularMeals(Set<Order> todayOrders, Set<Order> ordersOfMonth, Optional<TotalStatistic> totalStatistic) {
        Set<PopularMealsResponse> popularMealsCount = new HashSet<>();
        popularMealsCount.add(new PopularMealsResponse(TimeSpan.TODAY, getPopularMealsCount(todayOrders)));
        popularMealsCount.add(new PopularMealsResponse(TimeSpan.MONTH, getPopularMealsCount(ordersOfMonth)));
        Set<PopularMealsStatistic> totalPopulateMeals = totalStatistic.map(TotalStatistic::getPopularMeals).orElse(Collections.emptySet());
        popularMealsCount.add(new PopularMealsResponse(TimeSpan.TOTAL, getMapperPopularMeals(totalPopulateMeals)));
        return new AsyncResult<>(popularMealsCount);
    }

    private Set<PopularMealResponse> getPopularMealsCount(Set<Order> orders) {
        Map<String, Integer> countMeals = new HashMap<>();
        orders.stream()
                .map(Order::getMeals)
                .forEach(meals -> meals.forEach(meal -> {
                            countMeals.computeIfPresent(meal.getName(), (key, quantity) -> quantity + meal.getQuantity());
                            countMeals.putIfAbsent(meal.getName(), meal.getQuantity());
                        })
                );
        return countMeals.entrySet()
                .stream()
                .map(map -> new PopularMealResponse(map.getKey(), map.getValue()))
                .collect(Collectors.toSet());
    }

    private Set<PopularMealResponse> getMapperPopularMeals(Set<PopularMealsStatistic> popularMeals) {
        return popularMeals.stream()
                .map(popularMeal -> new PopularMealResponse(popularMeal.getMealName(), popularMeal.getCount()))
                .collect(Collectors.toSet());
    }
}
