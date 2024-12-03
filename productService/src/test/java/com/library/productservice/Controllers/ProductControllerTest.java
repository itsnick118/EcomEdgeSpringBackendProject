package com.library.productservice.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.library.productservice.Interface.IProductService;
import com.library.productservice.Models.Product;
import com.library.productservice.Repostories.ProductRepository;
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
    public void getAllProduct_success() throws Exception {
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

        Mockito.when(productService.getAllProducts()).thenReturn(mockProducts);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/products/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Product1"))
                .andExpect(jsonPath("$[1].title").value("Product2"));
    }

    @Test
    public void getProductById_success() throws Exception {
        ProductResponseDTO mockProduct = new ProductResponseDTO();
        mockProduct.setId(1L);
        mockProduct.setTitle("Product1");
        mockProduct.setDescription("Description1");
        mockProduct.setPrice(10.0);

        Mockito.when(productService.getProductById(1L))
                .thenReturn(mockProduct);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Product1"));
    }

  /*  @Test
    public void getProductById_ThrowsException() throws Exception {
        Mockito.when(productService.getProductById(1L))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found"));
    }*/

    @Test
    public void createProduct_success() throws Exception {
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setTitle("Product1");
        mockProduct.setDescription("Description1");
        mockProduct.setPrice(10.0);

        Mockito.when(productService.createProduct(ArgumentMatchers.any(CreateProductRequestDTO.class))).thenReturn(ProductResponseDTO.from(mockProduct));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(mockProduct.getTitle()));
    }

    @Test
    public void updateProduct_success() throws Exception {
        ProductResponseDTO mockProduct = new ProductResponseDTO();
        mockProduct.setId(1L);
        mockProduct.setTitle("Product1");
        mockProduct.setDescription("Description1");
        mockProduct.setPrice(10.0);

        Mockito.when(productService.updateProduct(anyLong(), ArgumentMatchers.any(CreateProductRequestDTO.class))).thenReturn(mockProduct);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(mockProduct.getTitle()));
    }

    @Test
    public void deleteProduct_success() throws Exception {
     /*   ProductResponseDTO mockProduct = new ProductResponseDTO();
        mockProduct.setId(1L);
        mockProduct.setTitle("Product1");
        mockProduct.setDescription("Description1");
        mockProduct.setPrice(10.0);

        Mockito.when(productService.deleteProduct(anyLong())).thenReturn(mockProduct);

        mockMvc.perform(delete("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(mockProduct.getTitle()));*/
    }
}