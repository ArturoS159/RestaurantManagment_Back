package com.przemarcz.restaurant.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
public class MealDto {
    private MealDto() {
    }

    @Value
    public static class OrderMealRequest {
        UUID id;
        Integer quantity;
    }

    @Value
    public static class CreateMealRequest {
        @Size(min = 2, max = 50, message = "Name must be in range 4-50")
        String name;
        @NotNull(message = "Price must be not null")
        @Digits(integer = 6, fraction = 2, message = "Price bad format")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.0")
        BigDecimal price;
        String image;
        @NotBlank(message = "Ingredients must be not blank")
        String ingredients;
        @NotNull(message = "Time must be not null")
//        @Digits(integer = 2, fraction = 2, message = "Time bad format") //TODO look at frontend
        @DecimalMin(value = "0.0", inclusive = false, message = "Time must be greater than 0.0")
        BigDecimal timeToDo;
    }

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
    }

    @Value
    public static class MealResponse {
        UUID id;
        String name;
        BigDecimal price;
        String image;
        String ingredients;
        BigDecimal timeToDo;
    }
}
