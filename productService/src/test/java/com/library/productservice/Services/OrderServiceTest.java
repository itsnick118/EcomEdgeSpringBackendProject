package com.library.productservice.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.library.productservice.Exceptions.OrderNotFoundException;
import com.library.productservice.Exceptions.ProductNotFoundException;
import com.library.productservice.Models.Order;
import com.library.productservice.Models.OrderItem;
import com.library.productservice.Models.Product;
import com.library.productservice.Repostories.OrderItemRepository;
import com.library.productservice.Repostories.OrderRepository;
import com.library.productservice.Repostories.ProductRepository;
import com.library.productservice.dto.OrderItemRequestDTO;
import com.library.productservice.dto.OrderRequestDTO;
import com.library.productservice.dto.OrderResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
public class OrderServiceTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectWriter = objectMapper.writer();
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(orderService).build();
    }

    @Test
    public void placeOrder_success() throws ProductNotFoundException {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setName("Test Order");
        orderRequestDTO.setAddress("Test Address");
        orderRequestDTO.setPhone("1234567890");

        OrderItemRequestDTO item1 = new OrderItemRequestDTO();
        item1.setProductId(1L);
        item1.setQuantity(2);

        orderRequestDTO.setItems(List.of(item1));

        Product product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(50.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        OrderResponseDTO orderResponseDTO = orderService.placeOrder(orderRequestDTO);

        assertNotNull(orderResponseDTO);
        assertEquals("Test Order", orderResponseDTO.getName());
        assertEquals("Test Address", orderResponseDTO.getAddress());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void placeOrder_productNotFound() {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setName("Test Order");
        orderRequestDTO.setAddress("Test Address");
        orderRequestDTO.setPhone("1234567890");

        OrderItemRequestDTO item1 = new OrderItemRequestDTO();
        item1.setProductId(1L);
        item1.setQuantity(2);

        orderRequestDTO.setItems(List.of(item1));

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> orderService.placeOrder(orderRequestDTO));
    }

    @Test
    public void getOrderDetails_success() throws OrderNotFoundException {
        Long orderId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setName("Test Order");

        OrderItem orderItem = new OrderItem();
        orderItem.setProductTitle("Test Product");
        orderItem.setQuantity(2);

        order.setItems(List.of(orderItem));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderResponseDTO orderResponseDTO = orderService.getOrderDetails(orderId);

        assertNotNull(orderResponseDTO);
        assertEquals("Test Order", orderResponseDTO.getName());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    public void getOrderDetails_orderNotFound() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderDetails(orderId));
    }

    @Test
    public void doubleDecimal_test() {
        assertEquals(150.75, orderService.doubleDecimal(150.754), 0.01);
    }
}
