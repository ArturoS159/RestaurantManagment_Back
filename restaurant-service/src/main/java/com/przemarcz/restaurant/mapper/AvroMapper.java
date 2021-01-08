package com.przemarcz.restaurant.mapper;

import com.przemarcz.avro.MealAvro;
import com.przemarcz.avro.OrderAvro;
import com.przemarcz.avro.OrderType;
import com.przemarcz.avro.PaymentMethod;
import com.przemarcz.restaurant.dto.MealDto.OrderMealRequest;
import com.przemarcz.restaurant.model.Meal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.przemarcz.restaurant.dto.OrderDto.CreateOrderPersonalRequest;
import static com.przemarcz.restaurant.dto.OrderDto.CreateOrderUserRequest;

@Mapper(componentModel = "spring", uses = TextMapper.class, imports = {LocalDateTime.class, OrderType.class, PaymentMethod.class})
public interface AvroMapper {

    @Mapping(target = "meals", ignore = true)
    @Mapping(target = "id", expression = "java(UUID.randomUUID().toString())")
    @Mapping(target = "time", expression = "java(LocalDateTime.now().toString())")
    @Mapping(target = "comment", source = "order.comment", defaultValue = "")
    OrderAvro toOrderByUser(CreateOrderUserRequest order, UUID restaurantId, String userId, String restaurantName);

    @Mapping(target = "meals", ignore = true)
    @Mapping(target = "id", expression = "java(UUID.randomUUID().toString())")
    @Mapping(target = "time", expression = "java(LocalDateTime.now().toString())")
    @Mapping(target = "comment", source = "order.comment", defaultValue = "")
    OrderAvro toOrderByPersonal(CreateOrderPersonalRequest order, UUID restaurantId, String restaurantName);

    @Mapping(target = "name", source = "meal.name")
    @Mapping(target = "price", source = "meal.price")
    @Mapping(target = "image", source = "meal.image")
    @Mapping(target = "ingredients", source = "meal.ingredients")
    @Mapping(target = "timeToDo", source = "meal.timeToDo")
    MealAvro toMealAvro(OrderMealRequest mealDto, Meal meal);
}
