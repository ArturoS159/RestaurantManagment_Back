package com.przemarcz.restaurant.dto;

import com.przemarcz.avro.OrderType;
import com.przemarcz.avro.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class OrderDto {
    private String forename;
    private String surname;
    private String street;
    private String city;
    private String email;
    private String phoneNumber;
    private String postCode;
    private String comment;
    private OrderType orderType;
    private PaymentMethod paymentMethod;
    private List<MealDto> meals;
}
