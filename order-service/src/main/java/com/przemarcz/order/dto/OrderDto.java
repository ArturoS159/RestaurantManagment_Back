package com.przemarcz.order.dto;

import com.przemarcz.order.model.Meal;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderDto {
    private UUID id;
    private String forename;
    private String surname;
    private String city;
    private String street;
    private String postCode;
    private String phoneNumber;
    private String comment;
    private BigDecimal price;
    private UUID restaurantId;
    private List<Meal> meals;
}
