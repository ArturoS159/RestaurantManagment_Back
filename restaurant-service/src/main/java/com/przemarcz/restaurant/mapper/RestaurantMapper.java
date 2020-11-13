package com.przemarcz.restaurant.mapper;

import com.przemarcz.restaurant.dto.RestaurantDto;
import com.przemarcz.restaurant.model.Restaurant;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = MealMapper.class)
public interface RestaurantMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "meals", ignore = true)
    Restaurant toRestaurant(RestaurantDto restaurantDto, UUID ownerId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRestaurant(@MappingTarget Restaurant restaurant, RestaurantDto restaurantDto);

    RestaurantDto toRestaurantDto(Restaurant restaurant);
}
