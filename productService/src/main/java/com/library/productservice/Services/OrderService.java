package com.library.productservice.Services;

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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
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

    public double doubleDecimal(double num){
        return Math.round(num * Math.pow(10, 2)) / Math.pow(10, 2);
    }

    @Override
    public OrderResponseDTO getOrderDetails(Long orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId, orderId));
        List<OrderItemResponseDTO> orderItemResponseDTOs = new ArrayList<>();
        for(OrderItem orderItem : order.getItems()){
            orderItemResponseDTOs.add(OrderItemResponseDTO.from(orderItem));
        }
        return OrderResponseDTO.from(order, orderItemResponseDTOs);
    }
}
