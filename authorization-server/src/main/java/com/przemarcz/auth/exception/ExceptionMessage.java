package com.przemarcz.auth.exception;

import lombok.Getter;

@Getter
public enum ExceptionMessage {

    RECORD_NOT_FOUND("Record/s not found"),
    RECORD_ALREADY_EXIST("Record/s already exist"),
    ACTIVATED_BEFORE("Account was activated before"),
    INCORRECT_FIELDS("Provided fields are incorrect"),
    BAD_ACTIVATION_KEY("Bad activation key");

    private final String errorMessage;

    ExceptionMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
