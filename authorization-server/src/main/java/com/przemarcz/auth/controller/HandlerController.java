package com.przemarcz.auth.controller;

import com.przemarcz.auth.dto.ApiResponse;
import com.przemarcz.auth.dto.exception.NotFoundException;
import com.przemarcz.auth.dto.exception.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleExceptionNotFound(RuntimeException err) {
        final ApiResponse apiResponse = new ApiResponse(err.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiResponse> handleExceptionConflict(RuntimeException err) {
        final ApiResponse apiResponse = new ApiResponse(err.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }
}
