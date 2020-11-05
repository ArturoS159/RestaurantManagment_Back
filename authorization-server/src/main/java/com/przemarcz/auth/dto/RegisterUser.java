package com.przemarcz.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterUser {
    private String email;
    private String password;
    private String login;
}
