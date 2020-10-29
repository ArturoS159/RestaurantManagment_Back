package com.przemarcz.restaurant.mapper;

import com.przemarcz.avro.MealAvro;
import com.przemarcz.avro.OrderAvro;
import com.przemarcz.restaurant.dto.MealDto;
import com.przemarcz.restaurant.dto.OrderDto;
import com.przemarcz.restaurant.model.Meal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = TextMapper.class)
public interface AvroMapper {
    @Mapping(target = "meals", ignore = true)
    OrderAvro toOrderAvro(OrderDto orderDto, UUID restaurantId);

    @Mapping(target = "id", source = "meal.id")
    @Mapping(target = "name", source = "meal.name")
    @Mapping(target = "price", source = "meal.price")
    @Mapping(target = "image", source = "meal.image")
    @Mapping(target = "ingredients", source = "meal.ingredients")
    @Mapping(target = "timeToDo", source = "meal.timeToDo")
    MealAvro toMealAvro(MealDto mealDto, Meal meal);
}
