package com.library.productservice.dto;

import com.library.productservice.Models.Order;
import com.library.productservice.Models.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderItemResponseDTO {
    private String productTitle;
    private double price;
    private int quantity;

    public static OrderItemResponseDTO from(OrderItem orderItem){
        OrderItemResponseDTO orderItemResponseDTO= new OrderItemResponseDTO();
        orderItemResponseDTO.setProductTitle(orderItem.getProductTitle());
        orderItemResponseDTO.setPrice(orderItem.getPrice());
        orderItemResponseDTO.setQuantity(orderItem.getQuantity());
        return orderItemResponseDTO;
    }

}
