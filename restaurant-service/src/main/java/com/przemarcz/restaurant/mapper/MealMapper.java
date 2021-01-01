package com.przemarcz.restaurant.mapper;

import com.przemarcz.restaurant.dto.MealDto.CreateMealRequest;
import com.przemarcz.restaurant.dto.MealDto.MealResponse;
import com.przemarcz.restaurant.dto.MealDto.UpdateMealRequest;
import com.przemarcz.restaurant.model.Meal;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
public interface MealMapper {

    @Mapping(target = "price", source = "createMealRequest.price", qualifiedByName = "scaleBigDecimal")
    @Mapping(target = "category", expression = "java(createMealRequest.getCategory().toLowerCase())")
    Meal toMeal(CreateMealRequest createMealRequest, UUID restaurantId);

    @Mapping(target = "price", source = "updateMealRequest.price", qualifiedByName = "scaleBigDecimal")
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMeal(@MappingTarget Meal meal, UpdateMealRequest updateMealRequest);

    @Mapping(target = "price", source = "price", qualifiedByName = "scaleBigDecimal")
    MealResponse toMealResponse(Meal meal);

    @Named("scaleBigDecimal")
    default BigDecimal setScaleBigDecimal(BigDecimal value ) {
        return isNull(value) ? null : value.setScale( 2, RoundingMode.DOWN );
    }
}
