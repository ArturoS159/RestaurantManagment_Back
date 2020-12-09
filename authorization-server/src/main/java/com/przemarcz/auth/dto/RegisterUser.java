package com.przemarcz.auth.dto;

import lombok.Getter;

@Getter
public class RegisterUser {
    private String email;
    private String password;
    private String login;
}
