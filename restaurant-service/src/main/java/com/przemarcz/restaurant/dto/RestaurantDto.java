package com.przemarcz.restaurant.dto;

import com.przemarcz.restaurant.dto.WorkTimeDto.WorkTimeRequest;
import com.przemarcz.restaurant.dto.WorkTimeDto.WorkTimeResponse;
import com.przemarcz.restaurant.model.enums.RestaurantCategory;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RestaurantDto {
    private RestaurantDto(){
    }

    @Builder
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

    @Builder
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
        boolean paymentOnline;
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
    public static class AllRestaurantForOwnerResponse {
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
        boolean paymentOnline;
    }

    @Value
    public static class RestaurantForOwnerResponse {
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
        boolean paymentOnline;
    }

    @Value
    public static class RestaurantFilter {
        String name;
        String category;
        String city;
        BigDecimal rate;
        Boolean open;
    }
}
