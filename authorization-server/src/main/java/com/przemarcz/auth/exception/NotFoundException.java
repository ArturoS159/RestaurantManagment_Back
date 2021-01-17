package com.przemarcz.auth.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.getErrorMessage());
    }
}
