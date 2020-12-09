package com.przemarcz.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
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
