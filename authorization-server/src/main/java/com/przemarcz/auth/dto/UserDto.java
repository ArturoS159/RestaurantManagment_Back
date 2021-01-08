package com.przemarcz.auth.dto;

import lombok.Value;

import javax.validation.constraints.*;
import java.util.List;
import java.util.UUID;

public class UserDto {
    private UserDto(){}

    @Value
    public static class RegisterUserRequest {
        @NotBlank(message = "Email must be not blank")
        @Email(message = "It's not email!")
        @Size(max = 50, message = "Email must be lower than 50")
        String email;
        @NotBlank(message = "Password must be not blank")
        @Size(min = 4, max = 50, message = "Password must be in range! 4-50")
        String password;
        @NotBlank(message = "Login must be not blank")
        @Size(max = 50, message = "Login must be lower than 50")
        String login;
    }

    @Value
    public static class UpdateUserRequest {
        @Size(max = 50, message = "Forename must be lower than 50")
        String forename;
        @Size(max = 50, message = "Surname must be lower than 50")
        String surname;
        @Size(max = 50, message = "Street must be lower than 50")
        String street;
        @Size(max = 50, message = "City must be lower than 50")
        String city;
        @Pattern(regexp = "\\d{2}-\\d{3}", message = "It's not postcode")
        String postCode;
        @Pattern(regexp = "(?<!\\w)(\\(?(\\+|00)?48\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)", message = "It's not phone number")
        String phoneNumber;
        @Size(max = 20, message = "House number must be lower then 20")
        String houseNumber;
    }

    @Value
    public static class ActivationUserRequest {
        String login;
        String activationKey;
    }

    @Value
    public static class UserResponse {
        String email;
        String login;
        String forename;
        String surname;
        String street;
        String city;
        String postCode;
        String phoneNumber;
        String houseNumber;
    }

    @Value
    public static class WorkerResponse{
        UUID id;
        String email;
        String forename;
        String surname;
        String street;
        String city;
        String postCode;
        String phoneNumber;
        String houseNumber;
    }

    @Value
    public static class WorkerRestaurantResponse{
        List<UUID> restaurants;
    }

}
