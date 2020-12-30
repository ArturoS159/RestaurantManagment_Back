package com.przemarcz.order.mapper;

import com.przemarcz.order.dto.OrderDto.OrderForRestaurantResponse;
import com.przemarcz.order.dto.OrderDto.UpdateOrderRequest;
import com.przemarcz.order.model.Order;
import com.przemarcz.order.util.payumodels.PaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static com.przemarcz.order.dto.OrderDto.OrderForUserResponse;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderForRestaurantResponse toOrderForRestaurantResponse(Order order);
    OrderForUserResponse toOrderForUserResponse(Order order);
    void updateOrderPayUResponse(@MappingTarget Order order, PaymentResponse paymentResponse);
    void updateOrder(@MappingTarget Order order, UpdateOrderRequest updateOrderRequest);
}
