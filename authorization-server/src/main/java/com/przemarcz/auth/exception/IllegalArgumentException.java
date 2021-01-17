package com.przemarcz.auth.exception;

public class IllegalArgumentException extends RuntimeException {

    public IllegalArgumentException(ExceptionMessage message) {
        super(message.getErrorMessage());
    }
}
