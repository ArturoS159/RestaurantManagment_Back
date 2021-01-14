package com.przemarcz.order.mapper;

import com.przemarcz.avro.AddDelete;
import com.przemarcz.avro.PaymentAvro;
import com.przemarcz.order.model.RestaurantPayment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static com.przemarcz.order.dto.PaymentDto.CreatePaymentRequest;
import static com.przemarcz.order.dto.PaymentDto.PaymentResponse;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "id", source = "id")
    RestaurantPayment toPayment(CreatePaymentRequest createPaymentRequest, UUID id);

    PaymentResponse toPaymentResponse(RestaurantPayment restaurantPayment);

    @Mapping(target = "restaurantId", expression = "java(restaurantId.toString())")
    PaymentAvro toPaymentAvro(UUID restaurantId, AddDelete type);
}
