package com.library.productservice.Controllers;

import com.library.productservice.Exceptions.OrderNotFoundException;
import com.library.productservice.Exceptions.ProductNotFoundException;
import com.library.productservice.Interface.IOrderService;
import com.library.productservice.dto.OrderRequestDTO;
import com.library.productservice.dto.OrderResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/")
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO) throws ProductNotFoundException {
        OrderResponseDTO orderResponse = orderService.placeOrder(orderRequestDTO);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderDetails(@PathVariable("id") Long id) throws OrderNotFoundException {
        OrderResponseDTO orderResponse = orderService.getOrderDetails(id);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }
}
