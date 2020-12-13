package com.przemarcz.auth.exception;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException() {
        super("User already exist!");
    }

    public AlreadyExistException(String message) {
        super(message);
    }
}
