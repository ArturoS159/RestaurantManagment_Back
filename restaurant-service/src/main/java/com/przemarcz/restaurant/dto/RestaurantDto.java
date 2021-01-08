package com.przemarcz.restaurant.dto;

import com.przemarcz.restaurant.dto.WorkTimeDto.WorkTimeRequest;
import com.przemarcz.restaurant.dto.WorkTimeDto.WorkTimeResponse;
import com.przemarcz.restaurant.model.enums.RestaurantCategory;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RestaurantDto {
    private RestaurantDto(){
    }

    @Value
    public static class CreateRestaurantRequest {
        String name;
        Set<RestaurantCategory> category;
        List<WorkTimeRequest> worksTime;
        String image;
        String description;
        String nip;
        String regon;
        String city;
        String street;
        String postCode;
        String houseNumber;
        String phoneNumber;
    }

    @Value
    public static class UpdateRestaurantRequest {
        String name;
        Set<RestaurantCategory> category;
        List<WorkTimeRequest> worksTime;
        String image;
        String description;
        String city;
        String street;
        String postCode;
        String houseNumber;
        String phoneNumber;
    }

    @Value
    public static class GetAllRestaurantsForWorkerRequest {
        List<UUID> restaurants;
        String nothing;
    }

    @Value
    public static class AllRestaurantResponse {
        UUID id;
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
    public static class RestaurantResponse {
        UUID id;
        String name;
        Set<RestaurantCategory> category;
        String image;
        String description;
        String city;
        String street;
        String postCode;
        String houseNumber;
        String phoneNumber;
        BigDecimal rate;
        boolean paymentOnline;
    }

    @Value
    public static class AllRestaurantOwnerResponse {
        UUID id;
        String name;
        Set<RestaurantCategory> category;
        String nip;
        String regon;
        String image;
        String city;
        String street;
        String postCode;
        String houseNumber;
        String phoneNumber;
        BigDecimal rate;
    }

    @Value
    public static class RestaurantOwnerResponse {
        UUID id;
        String name;
        Set<RestaurantCategory> category;
        List<WorkTimeResponse> worksTime;
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
    public static class RestaurantFilter {
        String name;
        Set<RestaurantCategory> category;
        String city;
        BigDecimal rate;
        Boolean open;
    }
}
