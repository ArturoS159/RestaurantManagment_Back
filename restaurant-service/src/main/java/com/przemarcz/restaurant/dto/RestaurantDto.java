package com.przemarcz.restaurant.dto;

import com.przemarcz.restaurant.model.RestaurantCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class RestaurantDto {
    private UUID id;
    private String name;
    private Set<RestaurantCategory> category;
    private List<MealDto> mealList;
    private String image;
    private String city;
    private String street;
    private String houseNumber;
    private String phoneNumber;
}
