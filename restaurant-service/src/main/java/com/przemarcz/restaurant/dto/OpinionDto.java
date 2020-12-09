package com.przemarcz.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class OpinionDto {
    private BigDecimal rate;
    private String description;
}
