package com.przemarcz.auth.dto;

import com.przemarcz.auth.model.enums.RoleName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    private String email;
    private String password;
    private RoleName role;
    private String login;
    private String forename;
    private String surname;
    private String street;
    private String city;
    private String postCode;
    private String phoneNumber;
    private String houseNumber;
    private String identityNumber;
    private String nip;
    private String regon;
}
