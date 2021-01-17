package com.przemarcz.auth.exception;

public class AlreadyExistException extends RuntimeException {

    public AlreadyExistException(ExceptionMessage message) {
        super(message.getErrorMessage());
    }
}
