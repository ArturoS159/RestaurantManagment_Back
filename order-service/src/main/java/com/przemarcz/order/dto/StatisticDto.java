package com.przemarcz.order.dto;

import com.przemarcz.order.model.enums.OrderType;
import com.przemarcz.order.model.enums.PaymentMethod;
import com.przemarcz.order.model.enums.TimeSpan;
import lombok.Builder;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.przemarcz.order.dto.OrderDto.OrderStatisticResponse;

public class StatisticDto {
    private StatisticDto() {
    }

    @Builder
    @Value
    public static class StatisticResponse {
        List<OrderStatisticResponse> lastOrders;
        Set<ProfitResponse> profit;
        Set<CountOrderResponse> counts;
        Set<TypedCountOrderResponse> typedCounts;
        Set<TypedPaymentOrderResponse> paymentCount;
        Set<PopularMealsResponse> popularMeals;
    }

    @Value
    public static class ProfitResponse {
        TimeSpan time;
        BigDecimal price;
    }

    @Value
    public static class CountOrderResponse {
        TimeSpan time;
        long count;
    }

    @Value
    public static class TypedCountOrderResponse {
        TimeSpan time;
        OrderType type;
        long count;
    }

    @Value
    public static class TypedPaymentOrderResponse {
        TimeSpan time;
        PaymentMethod paymentMethod;
        long count;
    }

    @Value
    public static class PopularMealsResponse {
        TimeSpan time;
        Set<PopularMealResponse> meals;
    }

    @Value
    public static class PopularMealResponse {
        String meal;
        long count;
    }

    @Value
    public static class StatisticFilter {
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date;
    }
}
