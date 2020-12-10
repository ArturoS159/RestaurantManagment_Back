package com.przemarcz.restaurant.mapper;

import com.przemarcz.restaurant.dto.MealDto;
import com.przemarcz.restaurant.model.Meal;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
public interface MealMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", source = "mealDto.price", qualifiedByName = "scaleBigDecimal")
    Meal toMeal(MealDto mealDto, UUID restaurantId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurantId", ignore = true)
    @Mapping(target = "price", source = "mealDto.price", qualifiedByName = "scaleBigDecimal")
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMeal(@MappingTarget Meal meal, MealDto mealDto);

    @Mapping(target = "quantity", ignore = true)
    MealDto toMealDto(Meal meal);

    @Named("scaleBigDecimal")
    default BigDecimal setScaleBigDecimal(BigDecimal value ) {
        return isNull(value) ? null : value.setScale( 2, RoundingMode.DOWN );
    }
}
