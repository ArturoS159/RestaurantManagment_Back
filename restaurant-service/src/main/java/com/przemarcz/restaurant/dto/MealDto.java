package com.przemarcz.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class MealDto {
    private MealDto() {
    }

    @Builder
    @Value
    public static class CreateMealRequest {
        @NotBlank(message = "Name must be not blank")
        @Size(min = 2, max = 50, message = "Name must be in range 4-50")
        String name;
        @NotNull(message = "Price must be not null")
        @Digits(integer = 6, fraction = 2, message = "Price bad format")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.0")
        BigDecimal price;
        String image;
        @NotBlank(message = "Ingredients must be not blank")
        @Size(max = 300, message = "Ingredients must be lower than 300")
        String ingredients;
        @NotNull(message = "Time must be not null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Time must be greater than 0.0")
        BigDecimal timeToDo;
        @NotBlank(message = "Category must be not blank")
        @Size(max = 30, message = "Category must be lower than 30")
        String category;
    }

    @Builder
    @Value
    public static class UpdateMealRequest {
        @Size(min = 2, max = 50, message = "Name must be in range 4-50!")
        String name;
        @Digits(integer = 6, fraction = 2, message = "Price bad format")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.0")
        BigDecimal price;
        String image;
        String ingredients;
        @DecimalMin(value = "0.0", inclusive = false, message = "Time must be greater than 0.0")
        BigDecimal timeToDo;
        @Size(max = 30, message = "Category must be lower than 30")
        String category;
    }

    @Value
    public static class OrderMealRequest {
        UUID id;
        Integer quantity;
    }

    @Builder
    @Value
    public static class MealFilter {
        String category;
        BigDecimal fromPrice;
        BigDecimal toPrice;
        BigDecimal fromTime;
        BigDecimal toTime;
    }

    @Value
    public static class MealListResponse {
        List<MealResponse> meals;
    }

    @Value
    public static class MealsCategoryResponse {
        Set<String> category;
    }

    @Value
    public static class MealResponse {
        UUID id;
        String name;
        @JsonFormat(shape=JsonFormat.Shape.STRING)
        BigDecimal price;
        String image;
        String ingredients;
        BigDecimal timeToDo;
        String category;
    }
}
