package com.library.productservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDTO {
    private String name;
    private String address;
    private String phone;
    private String paymentStatus;
    private List<OrderItemRequestDTO> items;
}
