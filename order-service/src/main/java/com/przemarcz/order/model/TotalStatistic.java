package com.przemarcz.order.model;

import com.przemarcz.order.model.enums.OrderType;
import com.przemarcz.order.model.enums.PaymentMethod;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Entity
@Table(name = "statistics_total")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class TotalStatistic {
    @Id
    @Column(name = "restaurant_id")
    private UUID id;
    @Column(name = "total_profit")
    private BigDecimal totalProfit;
    @Column(name = "total_orders")
    private Long totalOrders;
    @Column(name = "total_comments")
    private Long totalComments;
    @Column(name = "total_payment_cash")
    private Long totalPaymentCash;
    @Column(name = "total_payment_online")
    private Long totalPaymentOnline;
    @Column(name = "total_order_in_local")
    private Long totalOrderInLocal;
    @Column(name = "total_order_take_away")
    private Long totalOrderTakeAway;
    @Column(name = "total_order_delivery")
    private Long totalOrderDelivery;
    @OneToMany(mappedBy = "totalStatistic", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PopularMealsStatistic> popularMeals = new HashSet<>();

    public void addStatistics(Order order) {
        totalProfit = totalProfit.add(order.getPrice());
        totalOrders = totalOrders + order.getMeals().size();
        addStatsOfOrderPaymentMethod(order.getPaymentMethod());
        addStatsOfOrderType(order.getOrderType());
        addStatsOfPopularMeals(order.getMeals());
    }

    private void addStatsOfOrderPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod.equals(PaymentMethod.CASH)) {
            totalPaymentCash++;
        } else {
            totalPaymentOnline++;
        }
    }

    private void addStatsOfOrderType(OrderType orderType) {
        switch (orderType) {
            case IN_LOCAL:
                totalOrderInLocal++;
                break;
            case TAKE_AWAY:
                totalOrderTakeAway++;
                break;
            case DELIVERY:
                totalOrderDelivery++;
                break;
        }
    }

    private void addStatsOfPopularMeals(List<Meal> meals) {
        if (isNull(popularMeals))
            popularMeals = new HashSet<>();

        Set<Meal> mealsToAdd = meals.stream()
                .filter(meal -> !popularMeals.stream()
                        .map(PopularMealsStatistic::getMealName)
                        .collect(Collectors.toSet())
                        .contains(meal.getName()))
                .collect(Collectors.toSet());


        Set<Meal> mealsToUpdate = meals.stream()
                .filter(meal -> popularMeals.stream()
                        .map(PopularMealsStatistic::getMealName)
                        .collect(Collectors.toSet())
                        .contains(meal.getName()))
                .collect(Collectors.toSet());

        for (Meal meal : mealsToUpdate) {
            popularMeals.stream().filter(popularMealsStatistic -> popularMealsStatistic.getMealName().equals(meal.getName()))
                    .findFirst().ifPresent(popularMealsStatistic -> popularMealsStatistic.increment(meal.getQuantity()));
        }
        addPopularMeals(mealsToAdd);
    }

    private void addPopularMeals(Set<Meal> meals) {
        if (isNull(popularMeals))
            popularMeals = new HashSet<>();
        Set<PopularMealsStatistic> mealsToAdd =
                meals.stream().map(meal -> PopularMealsStatistic.builder()
                        .id(UUID.randomUUID())
                        .mealId(meal.getId())
                        .mealName(meal.getName())
                        .count(Long.valueOf(meal.getQuantity()))
                        .totalStatistic(this)
                        .build()).collect(Collectors.toSet());
        popularMeals.addAll(mealsToAdd);
    }
}
