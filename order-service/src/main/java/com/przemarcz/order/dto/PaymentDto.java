package com.przemarcz.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {
    private String posId;
    private String md5;
    private String clientId;
    private String clientSecret;
}