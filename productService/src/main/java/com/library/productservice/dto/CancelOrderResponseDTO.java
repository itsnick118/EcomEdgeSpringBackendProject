package com.library.productservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelOrderResponseDTO {
    private Long orderId;
    private String message;
}
