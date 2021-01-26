package com.przemarcz.order.mapper;

import com.przemarcz.order.dto.OrderDto.OrderForRestaurantResponse;
import com.przemarcz.order.dto.OrderDto.UpdateOrderRequest;
import com.przemarcz.order.model.Order;
import com.przemarcz.order.util.payumodels.PaymentResponse;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.stream.Collectors;

import static com.przemarcz.order.dto.OrderDto.OrderForUserResponse;
import static com.przemarcz.order.dto.OrderDto.OrderStatisticResponse;

@Mapper(componentModel = "spring", imports = {StringUtils.class, Collectors.class})
public interface OrderMapper {
    OrderForRestaurantResponse toOrderForRestaurantResponse(Order order);

    OrderForUserResponse toOrderForUserResponse(Order order);

    @Mapping(target = "meals", expression = "java(StringUtils.join(order.getMeals().stream().map(Meal::getName).collect(Collectors.toSet()), \", \"))")
    OrderStatisticResponse toOrderStatisticResponse(Order order);

    void updateOrderPayUResponse(@MappingTarget Order order, PaymentResponse paymentResponse);

    void updateOrder(@MappingTarget Order order, UpdateOrderRequest updateOrderRequest);
}
