package com.library.paymentgatewayservice.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitiatePaymentRequestDto {
    private Long orderId;
    private double amount;
}
