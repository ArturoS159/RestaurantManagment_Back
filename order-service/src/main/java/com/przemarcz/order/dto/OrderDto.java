package com.przemarcz.order.dto;

import com.przemarcz.order.model.enums.OrderStatus;
import com.przemarcz.order.model.enums.OrderType;
import com.przemarcz.order.model.enums.PaymentMethod;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.przemarcz.order.dto.MealDto.MealResponse;


public class OrderDto {
    private OrderDto() {
    }

    //TODO add validation
    @Value
    public static class UpdateOrderRequest {
        String forename;
        String surname;
        String city;
        String street;
        String postCode;
        String phoneNumber;
        String comment;
        PaymentMethod paymentMethod;
        OrderType orderType;
        OrderStatus orderStatus;
        boolean payed;
    }

    @Value
    public static class OrderForRestaurantResponse {
        UUID id;
        String forename;
        String surname;
        String city;
        String street;
        String postCode;
        String phoneNumber;
        String comment;
        PaymentMethod paymentMethod;
        OrderType orderType;
        OrderStatus orderStatus;
        LocalDateTime time;
        UUID userId;
        BigDecimal price;
        List<MealResponse> meals;
        boolean payed;
    }

    @Value
    public static class OrderForUserResponse {
        UUID id;
        UUID restaurantId;
        String forename;
        String surname;
        String city;
        String street;
        String postCode;
        String phoneNumber;
        String comment;
        PaymentMethod paymentMethod;
        OrderType orderType;
        OrderStatus orderStatus;
        LocalDateTime time;
        BigDecimal price;
        List<MealResponse> meals;
        boolean payed;
    }

    @Value
    public static class OrderFilter {
        PaymentMethod paymentMethod;
        OrderType orderType;
        OrderStatus orderStatus;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate fromDay;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate toDay;
        BigDecimal fromPrice;
        BigDecimal toPrice;
        Boolean payed;
        Boolean archived;
    }
}
