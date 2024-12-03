package com.library.productservice.dto;

import com.library.productservice.Models.Order;
import com.library.productservice.Models.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String paymentStatus;
    private double totalAmount;
    private List<OrderItemResponseDTO> items;

    public static OrderResponseDTO from(Order order, List<OrderItemResponseDTO> orderItemResponseDTOs){
        OrderResponseDTO orderResponseDTO= new OrderResponseDTO();
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setItems(orderItemResponseDTOs);
        orderResponseDTO.setTotalAmount(order.getTotalAmount());
        orderResponseDTO.setName(order.getName());
        orderResponseDTO.setAddress(order.getAddress());
        orderResponseDTO.setPhone(order.getPhone());
        orderResponseDTO.setPaymentStatus(order.getPaymentStatus());
        return orderResponseDTO;
    }
}
