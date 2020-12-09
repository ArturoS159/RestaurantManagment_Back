package com.przemarcz.auth.dto;

import lombok.Getter;

@Getter
public class UserActivation {
    private String login;
    private String activationKey;
}
