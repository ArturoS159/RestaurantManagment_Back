package com.przemarcz.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
public class MealDto {
    private UUID id;
    private String name;
    private BigDecimal price;
    private String image;
    private String ingredients;
    private BigDecimal timeToDo;
    private Integer quantity;
}
