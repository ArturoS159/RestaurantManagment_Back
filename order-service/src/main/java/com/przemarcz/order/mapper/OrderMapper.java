package com.przemarcz.order.mapper;

import com.przemarcz.order.model.Order;
import com.przemarcz.order.util.payumodels.PaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static com.przemarcz.order.dto.OrderDto.OrderResponse;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toOrderDto(Order order);

    void updateOrderPayUResponse(@MappingTarget Order order, PaymentResponse paymentResponse);
}
