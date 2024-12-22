package com.library.productservice.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.productservice.Exceptions.OrderNotFoundException;
import com.library.productservice.Exceptions.ProductNotFoundException;
import com.library.productservice.Interface.IOrderService;
import com.library.productservice.Models.Order;
import com.library.productservice.Models.OrderItem;
import com.library.productservice.Models.PaymentStatus;
import com.library.productservice.Models.Product;
import com.library.productservice.Repostories.OrderItemRepository;
import com.library.productservice.Repostories.OrderRepository;
import com.library.productservice.Repostories.ProductRepository;
import com.library.productservice.dto.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO) throws ProductNotFoundException {
        Order order = new Order();
        order.setName(orderRequestDTO.getName());
        order.setAddress(orderRequestDTO.getAddress());
        order.setPhone(orderRequestDTO.getPhone());
        order.setPaymentStatus(String.valueOf(PaymentStatus.PENDING));

        double totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequestDTO item : orderRequestDTO.getItems()) {
            Optional<Product> product = productRepository.findById(item.getProductId());
            if (product.isPresent()) {
                OrderItem orderItem = new OrderItem();
                double price = product.get().getPrice();
                orderItem.setPrice(price);
                orderItem.setProductTitle(product.get().getTitle());
                orderItem.setProductId(item.getProductId());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setOrder(order);
                totalAmount += price * item.getQuantity();
                orderItems.add(orderItem);
            } else {
                throw new ProductNotFoundException("Product not found with ID: " + item.getProductId(), item.getProductId());
            }
        }

        order.setItems(orderItems);
        order.setTotalAmount(doubleDecimal(totalAmount));
        orderRepository.save(order);

        List<OrderItemResponseDTO> orderItemResponseDTOs = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            orderItemResponseDTOs.add(OrderItemResponseDTO.from(orderItem));
        }
        return OrderResponseDTO.from(order, orderItemResponseDTOs);
    }

    public double doubleDecimal(double num) {
        return Math.round(num * Math.pow(10, 2)) / Math.pow(10, 2);
    }

    @Override
    public OrderResponseDTO getOrderDetails(Long orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId, orderId));
        List<OrderItemResponseDTO> orderItemResponseDTOs = new ArrayList<>();
        for (OrderItem orderItem : order.getItems()) {
            orderItemResponseDTOs.add(OrderItemResponseDTO.from(orderItem));
        }
        return OrderResponseDTO.from(order, orderItemResponseDTOs);
    }

    @KafkaListener(topics = "updatePaymentStatus", groupId = "paymentService")
    public void handleOrderPaymentEvent(String message) throws JsonProcessingException {
        OrderPaymentUpdateDTO orderPaymentUpdateDTO = objectMapper.readValue(
                message,
                OrderPaymentUpdateDTO.class
        );
        Optional<Order> fetchOrderOptional = orderRepository.findById(orderPaymentUpdateDTO.getOrderId());
        if (fetchOrderOptional.isPresent()) {
            Order order = fetchOrderOptional.get();
            if ("Success".equals(orderPaymentUpdateDTO.getMessage())) {
                order.setPaymentStatus(String.valueOf(PaymentStatus.SUCCESS));
            } else if ("Fail".equals(orderPaymentUpdateDTO.getMessage())) {
                order.setPaymentStatus(String.valueOf(PaymentStatus.FAILED));
            }
            orderRepository.save(order);
            System.out.println("paymentupdated");
        }
    }
}
