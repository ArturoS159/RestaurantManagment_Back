package com.przemarcz.order.mapper;

import com.przemarcz.avro.AddDelete;
import com.przemarcz.avro.PaymentAvro;
import com.przemarcz.order.dto.PaymentDto;
import com.przemarcz.order.model.RestaurantPayment;
import org.mapstruct.*;

import java.util.UUID;

import static com.przemarcz.order.dto.PaymentDto.*;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    RestaurantPayment toPayment(CreatePaymentRequest createPaymentRequest, UUID id);

    PaymentResponse toPaymentResponse(RestaurantPayment restaurantPayment);

    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePayment(@MappingTarget RestaurantPayment restaurantPayment, PaymentDto paymentDto, UUID id);

    @Mapping(target = "restaurantId", expression = "java(restaurantId.toString())")
    PaymentAvro toPaymentAvro(UUID restaurantId, AddDelete type);
}
