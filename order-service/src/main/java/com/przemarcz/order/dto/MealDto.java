package com.przemarcz.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class MealDto {
    private UUID id;
    private String name;
    private BigDecimal price;
    private String image;
    private String ingredients;
    private BigDecimal timeToDo;
    private Integer quantity;
}
