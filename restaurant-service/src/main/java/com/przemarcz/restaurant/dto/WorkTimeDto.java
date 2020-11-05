package com.przemarcz.restaurant.dto;

import com.przemarcz.restaurant.model.enums.Days;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class WorkTimeDto {
    private Days day;
    private LocalTime from;
    private LocalTime to;
}
