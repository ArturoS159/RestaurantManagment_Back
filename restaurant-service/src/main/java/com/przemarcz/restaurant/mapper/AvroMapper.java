package com.przemarcz.restaurant.mapper;

import com.przemarcz.avro.MealAvro;
import com.przemarcz.avro.OrderAvro;
import com.przemarcz.avro.OrderType;
import com.przemarcz.avro.PaymentMethod;
import com.przemarcz.restaurant.dto.MealDto;
import com.przemarcz.restaurant.dto.OrderDto;
import com.przemarcz.restaurant.model.Meal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = TextMapper.class, imports = {LocalDateTime.class, OrderType.class, PaymentMethod.class})
public interface AvroMapper {
    @Mapping(target = "meals", ignore = true)
    @Mapping(target = "id", expression = "java(UUID.randomUUID().toString())")
    @Mapping(target = "time", expression = "java(LocalDateTime.now().toString())")
    @Mapping(target = "orderType", expression = "java(OrderType.DELIVERY)")
    @Mapping(target = "paymentMethod", expression = "java(PaymentMethod.CASH)")
    @Mapping(target = "comment", source = "orderDto.comment", defaultValue = "")
    OrderAvro toOrderAvro(OrderDto orderDto, UUID restaurantId, String userId);

    @Mapping(target = "name", source = "meal.name")
    @Mapping(target = "price", source = "meal.price")
    @Mapping(target = "image", source = "meal.image")
    @Mapping(target = "ingredients", source = "meal.ingredients")
    @Mapping(target = "timeToDo", source = "meal.timeToDo")
    MealAvro toMealAvro(MealDto mealDto, Meal meal);
}
