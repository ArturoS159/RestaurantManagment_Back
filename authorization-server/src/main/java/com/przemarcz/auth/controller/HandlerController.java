package com.przemarcz.auth.controller;

import com.przemarcz.auth.dto.ApiResponse;
import com.przemarcz.auth.exception.AlreadyExistException;
import com.przemarcz.auth.exception.NotFoundException;
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

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ApiResponse> handleExceptionConflict(RuntimeException err) {
        final ApiResponse apiResponse = new ApiResponse(err.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgument(RuntimeException err) {
        final ApiResponse apiResponse = new ApiResponse(err.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
