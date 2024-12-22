package com.library.productservice.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.library.productservice.Interface.IProductService;
import com.library.productservice.dto.CreateProductRequestDTO;
import com.library.productservice.dto.ProductResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class ProductControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private IProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void getProductById_success() throws Exception {
        ProductResponseDTO mockProduct = new ProductResponseDTO();
        mockProduct.setId(1L);
        mockProduct.setTitle("Product1");
        mockProduct.setDescription("Description1");
        mockProduct.setPrice(10.0);

        when(productService.getProductById(1L))
                .thenReturn(mockProduct);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Product1"));
    }

    @Test
    public void getAllProducts_success() throws Exception {
        ProductResponseDTO product1 = new ProductResponseDTO();
        product1.setId(1L);
        product1.setTitle("Product1");
        product1.setDescription("Description1");
        product1.setPrice(10.0);

        ProductResponseDTO product2 = new ProductResponseDTO();
        product2.setId(2L);
        product2.setTitle("Product2");
        product2.setDescription("Description2");
        product2.setPrice(20.0);

        List<ProductResponseDTO> mockProducts = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(mockProducts);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/products/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Product1"))
                .andExpect(jsonPath("$[1].title").value("Product2"));
    }

    @Test
    public void createProduct_success() throws Exception {
        CreateProductRequestDTO createProductRequest = new CreateProductRequestDTO();
        createProductRequest.setTitle("Product1");
        createProductRequest.setDescription("Description1");
        createProductRequest.setPrice(10.0);
        createProductRequest.setImage("image1.png");
        createProductRequest.setCategoryId(1L);

        ProductResponseDTO mockProductResponse = new ProductResponseDTO();
        mockProductResponse.setId(1L);
        mockProductResponse.setTitle("Product1");
        mockProductResponse.setDescription("Description1");
        mockProductResponse.setPrice(10.0);

        when(productService.createProduct(ArgumentMatchers.any(CreateProductRequestDTO.class)))
                .thenReturn(mockProductResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/products/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockProductResponse.getId()))
                .andExpect(jsonPath("$.title").value(mockProductResponse.getTitle()))
                .andExpect(jsonPath("$.description").value(mockProductResponse.getDescription()))
                .andExpect(jsonPath("$.price").value(mockProductResponse.getPrice()));
    }

    @Test
    public void updateProduct_success() throws Exception {
        ProductResponseDTO mockProduct = new ProductResponseDTO();
        mockProduct.setId(1L);
        mockProduct.setTitle("Product1");
        mockProduct.setDescription("Description1");
        mockProduct.setPrice(10.0);

        when(productService.updateProduct(anyLong(), ArgumentMatchers.any(CreateProductRequestDTO.class))).thenReturn(mockProduct);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(mockProduct.getTitle()));
    }

    @Test
    public void deleteProduct_success() throws Exception {
        Long productId = 1L;
        String successMessage = "Product with ID " + productId + " has been successfully deleted.";

        when(productService.deleteProduct(productId)).thenReturn(successMessage);

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));
    }
}