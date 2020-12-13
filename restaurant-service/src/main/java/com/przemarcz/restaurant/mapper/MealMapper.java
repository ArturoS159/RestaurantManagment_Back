package com.przemarcz.restaurant.mapper;

import com.przemarcz.restaurant.dto.MealDto.UpdateMealRequest;
import com.przemarcz.restaurant.model.Meal;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static com.przemarcz.restaurant.dto.MealDto.*;
import static com.przemarcz.restaurant.dto.MealDto.MealResponse;
import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
public interface MealMapper {

    @Mapping(target = "price", source = "mealDto.price", qualifiedByName = "scaleBigDecimal")
    Meal toMeal(CreateMealRequest mealDto, UUID restaurantId);

    @Mapping(target = "price", source = "mealDto.price", qualifiedByName = "scaleBigDecimal")
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMeal(@MappingTarget Meal meal, UpdateMealRequest mealDto);

    MealResponse toMealResponse(Meal meal);

    @Named("scaleBigDecimal")
    default BigDecimal setScaleBigDecimal(BigDecimal value ) {
        return isNull(value) ? null : value.setScale( 2, RoundingMode.DOWN );
    }
}
