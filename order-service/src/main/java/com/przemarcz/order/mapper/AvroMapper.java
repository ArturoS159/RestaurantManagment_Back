package com.przemarcz.order.mapper;

import com.przemarcz.avro.MealAvro;
import com.przemarcz.avro.OrderAvro;
import com.przemarcz.order.model.Meal;
import com.przemarcz.order.model.Order;
import com.przemarcz.order.model.enums.OrderStatus;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = TextMapper.class, imports = {OrderStatus.class, UUID.class})
public interface AvroMapper {
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "orderStatus", expression = "java(OrderStatus.IN_PROGRESS)")
    Order toOrder(OrderAvro orderAvro);

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    Meal toMeal(MealAvro mealAvro);
}
