package com.przemarcz.order.mapper;

import com.przemarcz.order.dto.PaymentDto;
import com.przemarcz.order.model.RestaurantPayment;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    RestaurantPayment toPayment(PaymentDto paymentDto, UUID id);

    PaymentDto toPaymentDto(RestaurantPayment restaurantPayment);

    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePayment(@MappingTarget RestaurantPayment restaurantPayment, PaymentDto paymentDto, UUID id);
}
