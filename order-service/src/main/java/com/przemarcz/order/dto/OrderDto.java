package com.przemarcz.order.dto;

import com.przemarcz.order.model.enums.OrderType;
import com.przemarcz.order.model.enums.PaymentMethod;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.przemarcz.order.dto.MealDto.MealResponse;


public class OrderDto {
    private OrderDto() {
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
        LocalDateTime time;
        UUID userId;
        BigDecimal price;
        List<MealResponse> meals;
        boolean payed;
    }

    @Value
    public static class OrderForUserResponse {
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
        LocalDateTime time;
        BigDecimal price;
        List<MealResponse> meals;
        boolean payed;
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
        boolean payed;
    }
}
