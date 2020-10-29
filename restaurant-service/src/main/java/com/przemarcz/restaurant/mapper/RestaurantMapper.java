package com.przemarcz.restaurant.mapper;

import com.przemarcz.restaurant.dto.RestaurantDto;
import com.przemarcz.restaurant.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = MealMapper.class)
public interface RestaurantMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "meals", ignore = true)
    Restaurant toRestaurant(RestaurantDto restaurantDto, UUID ownerId);

    RestaurantDto toRestaurantDto(Restaurant restaurant);
}
