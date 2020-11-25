package com.przemarcz.order.mapper;

import com.przemarcz.avro.OrderAvro;
import com.przemarcz.order.dto.OrderDto;
import com.przemarcz.order.model.Order;
import com.przemarcz.order.util.payumodels.PaymentResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = TextMapper.class)
public interface OrderMapper {
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    Order toOrder(OrderAvro orderAvro);

    OrderDto toOrderDto(Order order);

    void updateOrderPayUResponse(@MappingTarget Order order, PaymentResponse paymentResponse);
}
