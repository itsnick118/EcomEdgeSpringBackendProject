package com.library.productservice.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.library.productservice.Exceptions.ProductNotFoundException;
import com.library.productservice.Exceptions.ProductsNotFoundException;
import com.library.productservice.Models.Category;
import com.library.productservice.Models.Product;
import com.library.productservice.Repostories.CategoryRepository;
import com.library.productservice.Repostories.ProductRepository;
import com.library.productservice.dto.CreateProductRequestDTO;
import com.library.productservice.dto.ProductResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
public class SelfProductServiceTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private SelfProductService selfProductService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(selfProductService).build();
    }

    @Test
    public void createProduct_success() {
        Long categoryId = 1L;
        CreateProductRequestDTO requestDTO = new CreateProductRequestDTO();
        requestDTO.setCategoryId(categoryId);
        requestDTO.setTitle("Test Product");
        requestDTO.setDescription("Product Description");
        requestDTO.setPrice(100.0);
        requestDTO.setImage("product-image.jpg");

        Category category = new Category();
        category.setId(categoryId);
        category.setTitle("Test Category");

        Product product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setDescription("Product Description");
        product.setPrice(100.0);
        product.setImage("product-image.jpg");
        product.setCategory(category);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO savedProduct = selfProductService.createProduct(requestDTO);

        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getTitle());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void getAllProducts_success() throws ProductsNotFoundException {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("Test Category");

        Product product1 = new Product();
        product1.setTitle("Product1");
        product1.setId(1L);
        product1.setCategory(category);

        Product product2 = new Product();
        product2.setTitle("Product2");
        product2.setId(2L);
        product2.setCategory(category);

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<ProductResponseDTO> products = selfProductService.getAllProducts();

        assertEquals(2, products.size());
        assertEquals("Product1", products.get(0).getTitle());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void getProductById_success() throws ProductNotFoundException {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("Test Category");

        Long productId = 1L;
        Product product = new Product();
        product.setTitle("Test Product");
        product.setId(productId);
        product.setCategory(category);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductResponseDTO fetchedProduct = selfProductService.getProductById(productId);

        assertNotNull(fetchedProduct);
        assertEquals("Test Product", fetchedProduct.getTitle());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void updateProduct_success() throws ProductNotFoundException {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("Test Category");
        Long productId = 1L;
        CreateProductRequestDTO requestDTO = new CreateProductRequestDTO();
        requestDTO.setTitle("Updated Product");
        requestDTO.setDescription("Updated Description");
        requestDTO.setPrice(150.0);
        requestDTO.setImage("updated-image.jpg");

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setTitle("Old Product");
        existingProduct.setDescription("Old Description");
        existingProduct.setPrice(100.0);
        existingProduct.setImage("old-image.jpg");
        existingProduct.setCategory(category);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        ProductResponseDTO updatedProduct = selfProductService.updateProduct(productId, requestDTO);

        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getTitle());
        assertEquals("Updated Description", updatedProduct.getDescription());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void deleteProduct_success() throws ProductNotFoundException {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        String result = selfProductService.deleteProduct(productId);

        assertEquals("Product with ID " + productId + " has been successfully deleted.", result);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    public void findAllProducts_success() throws ProductsNotFoundException {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("Test Category");
        int pageNumber = 0;
        int pageSize = 10;
        Product product1 = new Product();
        product1.setTitle("Product1");
        product1.setId(1L);
        product1.setCategory(category);

        Product product2 = new Product();
        product2.setTitle("Product2");
        product2.setId(2L);
        product2.setCategory(category);

        when(productRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(product1, product2)));

        var productsPage = selfProductService.findAllProducts(pageNumber, pageSize);

        assertNotNull(productsPage);
        assertEquals(2, productsPage.getTotalElements());
        assertEquals("Product1", productsPage.getContent().get(0).getTitle());
        verify(productRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    public void getProducts_byPriceRange_success() {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("Test Category");
        double minPrice = 50.0;
        double maxPrice = 150.0;
        Product product1 = new Product();
        product1.setPrice(100.0);
        product1.setCategory(category);

        Product product2 = new Product();
        product2.setPrice(150.0);
        product2.setCategory(category);

        when(productRepository.findByPriceBetween(minPrice, maxPrice)).thenReturn(List.of(product1, product2));

        List<ProductResponseDTO> products = selfProductService.getProducts(minPrice, maxPrice);

        assertEquals(2, products.size());
        assertEquals(100.0, products.get(0).getPrice());
        assertEquals(150.0, products.get(1).getPrice());
        verify(productRepository, times(1)).findByPriceBetween(minPrice, maxPrice);
    }
}


