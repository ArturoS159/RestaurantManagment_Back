package com.przemarcz.restaurant.dto;

import com.przemarcz.restaurant.model.enums.RestaurantCategory;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RestaurantDto {

    @Value
    public static class CreateRestaurantRequest {
        String name;
        Set<RestaurantCategory> category;
        List<WorkTimeDto> worksTime;
        String image;
        String description;
        String nip;
        String regon;
        String city;
        String street;
        String postCode;
        String houseNumber;
        String phoneNumber;
        BigDecimal rate;
    }

    @Value
    public static class AllRestaurantResponse {
        String name;
        Set<RestaurantCategory> category;
        String image;
        String city;
        String street;
        String postCode;
        String houseNumber;
        String phoneNumber;
        BigDecimal rate;
    }

    @Value
    public static class AllRestaurantOwnerResponse {
        String name;
        Set<RestaurantCategory> category;
        String image;
        String city;
        String street;
        String postCode;
        String houseNumber;
        String phoneNumber;
        BigDecimal rate;
    }

    @Value
    public static class RestaurantFilter {
        String name;
        Set<RestaurantCategory> category;
        String city;
        BigDecimal rate;
        Boolean open;
    }

    private UUID id;
    private String name;
    private Set<RestaurantCategory> category;
    private List<MealDto> meals;
    private List<WorkTimeDto> worksTime;
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
