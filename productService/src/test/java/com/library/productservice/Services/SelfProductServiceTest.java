package com.library.productservice.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.library.productservice.Exceptions.ProductNotFoundException;
import com.library.productservice.Models.Product;
import com.library.productservice.Repostories.ProductRepository;
import com.library.productservice.dto.CreateProductRequestDTO;
import com.library.productservice.dto.ProductResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
public class SelfProductServiceTest {
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SelfProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productService).build();
    }

    @Test
    public void createProduct_success() {
        CreateProductRequestDTO product = new CreateProductRequestDTO();
        product.setTitle("Test Product");
        product.setPrice(100.0);

        when(productRepository.save(Product.from(product))).thenReturn(Product.from(product));
        ProductResponseDTO savedProduct = productService.createProduct(product);

        assertEquals(product, savedProduct);
        assertEquals(product.getTitle(), savedProduct.getTitle());
        verify(productRepository).save(Product.from(product));
    }

    @Test
    public void getAllProducts_success() throws ProductNotFoundException {
        Product product1 = new Product();
        product1.setTitle("Test Product1");
        product1.setPrice(100.0);

        Product product2 = new Product();
        product2.setTitle("Test Product2");
        product2.setPrice(200.0);

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<ProductResponseDTO> products = productService.getAllProducts();

        assertEquals(2, products.size());
        assertEquals(product1.getTitle(), products.get(0).getTitle());
        verify(productRepository).findAll();
    }

    @Test
    public void getProductById_success() throws ProductNotFoundException {
        Product product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(100.0);
        product.setCreatedAt(null);
        product.setLastModifiedAt(null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponseDTO fetchedProduct = productService.getProductById(1L);

        assertNotNull(fetchedProduct);
        assertEquals(product, fetchedProduct);
        assertEquals(product.getTitle(), fetchedProduct.getTitle());
    }

    @Test
    public void updateProduct_success() throws ProductNotFoundException {
      /*  Product product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(100.0);
        product.setCreatedAt(null);
        product.setLastModifiedAt(null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        ProductResponseDTO updatedProduct = productService.updateProduct(1L, product);

        assertNotNull(updatedProduct);
        assertEquals(product, updatedProduct);
        assertEquals(product.getTitle(), updatedProduct.getTitle());
        verify(productRepository).save(product);*/
    }

    @Test
    public void deleteProduct_success() throws ProductNotFoundException {
        Product product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(100.0);
        product.setCreatedAt(null);
        product.setLastModifiedAt(null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productService.deleteProduct(1L);
        verify(productRepository).delete(product);
    }



}