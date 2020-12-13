package com.przemarcz.auth.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Resource not found!");
    }
}
