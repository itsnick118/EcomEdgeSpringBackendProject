package com.library.productservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequestDTO {
    private Long productId;
    private int quantity;
}
