package com.przemarcz.order.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

public class MealDto {
    private MealDto() {
    }

    @Value
    public static class MealResponse {
        UUID id;
        String name;
        BigDecimal price;
        String image;
        String ingredients;
        BigDecimal timeToDo;
        Integer quantity;
    }
}
