package com.przemarcz.auth.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDto {
    private UserDto(){}
    @Value
    public static class UserRegisterRequest {
        @Email(message = "It's not email!")
        @Size(min = 4, max = 50, message = "Email must be in range 4-50!")
        String email;
        @Size(min = 4, max = 50, message = "Password must be in range! 4-50")
        String password;
        @Size(min = 4, max = 50, message = "Login must be in range! 4-50")
        String login;
    }
    @Value
    public static class UserUpdateRequest {
        @Max(value = 50, message = "Forename must be lower then 50")
        String forename;
        @Max(value = 50, message = "Surname must be lower then 50")
        String surname;
        @Max(value = 50, message = "Street must be lower then 50")
        String street;
        @Max(value = 50, message = "City must be lower then 50")
        String city;
        @Pattern(regexp = "\\d{2}-\\d{3}", message = "It's not postcode")
        String postCode;
        @Pattern(regexp = "(?<!\\w)(\\(?(\\+|00)?48\\)?)?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}(?!\\w)", message = "It's not phone number")
        String phoneNumber;
        @Max(value = 20, message = "House number must be lower then 50")
        String houseNumber;
    }
    @Value
    public static class UserActivationRequest {
        String login;
        String activationKey;
    }
    @Value
    public static class UserResponse{
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
        String email;
        String forename;
        String surname;
        String street;
        String city;
        String postCode;
        String phoneNumber;
        String houseNumber;
    }

}
