package com.przemarcz.restaurant.dto;

import com.przemarcz.avro.OrderType;
import com.przemarcz.avro.PaymentMethod;
import lombok.Value;

import javax.validation.constraints.*;
import java.util.List;
import java.util.UUID;

import static com.przemarcz.restaurant.dto.MealDto.OrderMealRequest;


public class OrderDto {
    private OrderDto(){
    }

    @Value
    public static class CreateOrderUserRequest {
        @NotBlank(message = "Forename must be not blank")
        @Size(max = 50, message = "Forename be lower than 50")
        String forename;
        @NotBlank(message = "Surname must be not blank")
        @Size(max = 50, message = "Surname mbe lower than 50")
        String surname;
        @NotBlank(message = "Street must be not blank")
        @Size(max = 50, message = "Street be lower than 50")
        String street;
        @NotBlank(message = "City must be not blank")
        @Size(max = 50, message = "City must be lower than 50")
        String city;
        @NotBlank(message = "Email must be not blank")
        @Email(message = "It's not email!")
        @Size(max = 50, message = "Email must be lower than 50")
        String email;
        @NotBlank(message = "Phone-number must be not blank")
        @Pattern(regexp = "(?<!\\w)(\\(?(\\+|00)?48\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)", message = "It's not phone number")
        String phoneNumber;
        @NotBlank(message = "House-number must be not blank")
        @Size(max = 10, message = "House number must be lower than 10")
        String houseNumber;
        @NotBlank(message = "Surname must be not blank")
        @Pattern(regexp = "\\d{2}-\\d{3}", message = "It's not postcode")
        String postCode;
        @Size(max = 300, message = "Comment must be lower than 300")
        String comment;
        @NotNull(message = "Order-type must be not null")
        OrderType orderType;
        @NotNull(message = "Payment-method must be not null")
        PaymentMethod paymentMethod;
        @NotNull(message = "Meals must be not null")
        List<OrderMealRequest> meals;
    }

    @Value
    public static class CreateOrderPersonalRequest {
        @Size(max = 50, message = "Forename must be lower than 50")
        String forename;
        @Size(max = 50, message = "Surname mbe lower than 50")
        String surname;
        @Size(max = 50, message = "Street be lower than 50")
        String street;
        @Size(max = 50, message = "City must be lower than 50")
        String city;
        @Email(message = "It's not email!")
        @Size(max = 50, message = "Email must be lower than 50")
        String email;
        @Pattern(regexp = "(?<!\\w)(\\(?(\\+|00)?48\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)", message = "It's not phone number")
        String phoneNumber;
        @Size(max = 10, message = "Hose number must be lower than 10")
        String houseNumber;
        @Pattern(regexp = "\\d{2}-\\d{3}", message = "It's not postcode")
        String postCode;
        @Size(max = 300, message = "Comment must be lower than 300")
        String comment;
        @NotNull(message = "Order-type must be not null")
        OrderType orderType;
        @NotNull(message = "Payment-method must be not null")
        PaymentMethod paymentMethod;
        @NotNull(message = "Meals must be not null")
        List<OrderMealRequest> meals;
    }

    @Value
    public static class CreateOrderResponse {
        UUID orderId;
    }
}
