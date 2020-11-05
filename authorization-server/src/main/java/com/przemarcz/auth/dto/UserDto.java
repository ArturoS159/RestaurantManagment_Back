package com.przemarcz.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    private String email;
    private String login;
    private String forename;
    private String surname;
    private String street;
    private String city;
    private String postCode;
    private String phoneNumber;
    private String houseNumber;
}
