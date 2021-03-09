package com.przemarcz.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Getter
@RequiredArgsConstructor
public class ApiResponse {
    private final String message;
    private final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
}
