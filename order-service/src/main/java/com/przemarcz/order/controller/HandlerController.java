package com.przemarcz.order.controller;

import com.przemarcz.order.dto.ApiResponse;
import com.przemarcz.order.exception.AlreadyExistException;
import com.przemarcz.order.exception.NotFoundException;
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
    public ResponseEntity<ApiResponse> handleExceptionAlreadyExist(RuntimeException err) {
        final ApiResponse apiResponse = new ApiResponse(err.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleExceptionIllegalArgument(RuntimeException err) {
        final ApiResponse apiResponse = new ApiResponse(err.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
