package com.library.productservice.Controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.library.productservice.Interface.IOrderService;
import com.library.productservice.dto.OrderRequestDTO;
import com.library.productservice.dto.OrderResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class OrderControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private IOrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void placeOrder_success() throws Exception {
        OrderRequestDTO mockOrderRequest = new OrderRequestDTO();
        mockOrderRequest.setName("John Doe");
        mockOrderRequest.setAddress("123 Street");
        mockOrderRequest.setPhone("1234567890");
        mockOrderRequest.setPaymentStatus("PAID");
        mockOrderRequest.setItems(Collections.emptyList());

        OrderResponseDTO mockOrderResponse = new OrderResponseDTO();
        mockOrderResponse.setId(1L);
        mockOrderResponse.setName("John Doe");
        mockOrderResponse.setAddress("123 Street");
        mockOrderResponse.setPhone("1234567890");
        mockOrderResponse.setPaymentStatus("PAID");
        mockOrderResponse.setTotalAmount(100.0);

        when(orderService.placeOrder(any(OrderRequestDTO.class))).thenReturn(mockOrderResponse);

        mockMvc.perform(post("/orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockOrderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(mockOrderResponse.getId()))
                .andExpect(jsonPath("$.name").value(mockOrderResponse.getName()))
                .andExpect(jsonPath("$.totalAmount").value(mockOrderResponse.getTotalAmount()));
    }

    @Test
    public void getOrderDetails_success() throws Exception {
        OrderResponseDTO mockOrderResponse = new OrderResponseDTO();
        mockOrderResponse.setId(1L);
        mockOrderResponse.setName("John Doe");
        mockOrderResponse.setAddress("123 Street");
        mockOrderResponse.setPhone("1234567890");
        mockOrderResponse.setPaymentStatus("PAID");
        mockOrderResponse.setTotalAmount(100.0);

        when(orderService.getOrderDetails(anyLong())).thenReturn(mockOrderResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockOrderResponse.getId()))
                .andExpect(jsonPath("$.name").value(mockOrderResponse.getName()))
                .andExpect(jsonPath("$.paymentStatus").value(mockOrderResponse.getPaymentStatus()));
    }

}