package com.library.productservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderPaymentUpdateDTO {
    private Long orderId;
    private String message;
}