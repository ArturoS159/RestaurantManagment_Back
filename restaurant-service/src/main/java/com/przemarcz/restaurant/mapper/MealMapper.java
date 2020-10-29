package com.przemarcz.restaurant.mapper;

import com.przemarcz.restaurant.dto.MealDto;
import com.przemarcz.restaurant.model.Meal;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface MealMapper {
    @Mapping(target = "id", ignore = true)
    Meal toMeal(MealDto mealDto, UUID restaurantId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurantId", ignore = true)
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMeal(@MappingTarget Meal meal, MealDto mealDto);

    @Mapping(target = "quantity", ignore = true)
    MealDto toMealDto(Meal meal);
}
