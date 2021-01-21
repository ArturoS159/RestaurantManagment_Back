package com.przemarcz.order.dto;

import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.przemarcz.order.dto.OrderDto.*;

public class StatisticDto {
    private StatisticDto(){
    }

    @Value
    public static class StatisticResponse {
        List<OrderForRestaurantResponse> lastOrders;
        BigDecimal todayProfit;
        BigDecimal monthProfit;
        BigDecimal totalProfit;
    }

    @Value
    public static class StatisticFilter {
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date;
    }
}
