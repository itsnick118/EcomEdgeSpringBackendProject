package com.library.productservice.Interface;

import com.library.productservice.Exceptions.OrderNotFoundException;
import com.library.productservice.Exceptions.ProductNotFoundException;
import com.library.productservice.dto.CancelOrderResponseDTO;
import com.library.productservice.dto.OrderRequestDTO;
import com.library.productservice.dto.OrderResponseDTO;

import java.util.List;

public interface IOrderService {
    OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO) throws ProductNotFoundException;
    OrderResponseDTO getOrderDetails(Long orderId) throws OrderNotFoundException;
}
