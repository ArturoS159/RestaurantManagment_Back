package com.przemarcz.order.dto;

import lombok.Value;

public class PayUDto {

    private PayUDto(){
    }

    @Value
    public static class PayUUrlResponse {
        String payUUrl;
    }

}
