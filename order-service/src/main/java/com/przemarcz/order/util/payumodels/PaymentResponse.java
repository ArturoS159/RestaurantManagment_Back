package com.przemarcz.order.util.payumodels;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentResponse {
    private final String payUOrderId;
    private final String payUUrl;
}
