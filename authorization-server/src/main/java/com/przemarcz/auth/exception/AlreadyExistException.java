package com.przemarcz.auth.exception;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException() {
        super("Resource already exist!");
    }
}
