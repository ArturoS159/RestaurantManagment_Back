package com.przemarcz.restaurant.dto;

import com.przemarcz.restaurant.model.enums.RestaurantCategory;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class RestaurantDto {
    private UUID id;
    private String name;
    private Set<RestaurantCategory> category;
    private List<MealDto> meals;
//    private List<WorkTimeDto> worksTime;
    private String image;
    private String description;
    private String nip;
    private String regon;
    private String city;
    private String street;
    private String postCode;
    private String houseNumber;
    private String phoneNumber;
    private boolean open;
    private BigDecimal rate;
}
