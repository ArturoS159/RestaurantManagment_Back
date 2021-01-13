package com.przemarcz.restaurant.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class OpinionDto {
    private OpinionDto() {
    }

    @Builder
    @Value
    public static class CreateOpinionRequest {
        @NotNull(message = "Rate must be not null")
        @Digits(integer = 1, fraction = 1, message = "Rate bad format")
        @DecimalMax(value = "5.1", inclusive = false, message = "Rate must me smaller or equals than 5.0")
        @DecimalMin(value = "0.0", inclusive = false, message = "Rate must be greater than 0.0")
        BigDecimal rate;
        @Size(max = 500, message = "Description max size is 500")
        String description;
    }

    @Value
    public static class OpinionResponse {
        BigDecimal rate;
        String description;
    }
}
