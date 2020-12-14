package com.przemarcz.restaurant.dto;

import com.przemarcz.restaurant.model.enums.Days;
import lombok.Value;

import java.time.LocalTime;


public class WorkTimeDto {
    private WorkTimeDto(){
    }

    @Value
    public static class WorkTimeRequest {
        Days day;
        LocalTime from;
        LocalTime to;
    }

    @Value
    public static class WorkTimeResponse {
        Days day;
        LocalTime from;
        LocalTime to;
    }
}
