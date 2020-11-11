package com.przemarcz.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserActivation {
    private String login;
    private String activationKey;
}
