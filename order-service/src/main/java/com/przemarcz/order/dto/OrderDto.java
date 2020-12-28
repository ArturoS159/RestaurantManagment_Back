package com.przemarcz.order.dto;

import com.przemarcz.order.model.Meal;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.przemarcz.order.dto.MealDto.*;


public class OrderDto {
    private OrderDto() {
    }

    @Value
    public static class OrderResponse {
        UUID id;
        String forename;
        String surname;
        String city;
        String street;
        String postCode;
        String phoneNumber;
        String comment;
        String paymentMethod;
        String orderType;
        UUID userId;
        BigDecimal price;
        List<MealResponse> meals;
        boolean payed;
        String payUUrl;
        String payUOrderId;
    }
}
