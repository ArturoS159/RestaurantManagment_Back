package com.przemarcz.order.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;

public class PaymentDto {
    private PaymentDto() {
    }

    @Value
    public static class CreatePaymentRequest {
        @NotBlank(message = "Pos-id must be not blank")
        String posId;
        @NotBlank(message = "MD5 must be not blank")
        String md5;
        @NotBlank(message = "Client-id be not blank")
        String clientId;
        @NotBlank(message = "Client-secret must be not blank")
        String clientSecret;
    }

    @Value
    public static class PaymentResponse {
        String posId;
        String md5;
        String clientId;
        String clientSecret;
    }
}
