package com.library.productservice.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.library.productservice.Exceptions.CategoryNotFoundException;
import com.library.productservice.Models.Category;
import com.library.productservice.Models.Product;
import com.library.productservice.Repostories.CategoryRepository;
import com.library.productservice.Repostories.ProductRepository;
import com.library.productservice.dto.CategoryResponseDTO;
import com.library.productservice.dto.CreateCategoryRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
public class CategoryServiceTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(categoryService).build();
    }

    @Test
    public void createCategory_success() {
        CreateCategoryRequestDTO requestDTO = new CreateCategoryRequestDTO();
        requestDTO.setCategoryName("Test Category");

        Category category = new Category();
        category.setTitle("Test Category");
        category.setId(1L);

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO savedCategory = categoryService.createCategory(requestDTO);

        assertNotNull(savedCategory);
        assertEquals("Test Category", savedCategory.getCategoryName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void getAllCategories_success() {
        Category category1 = new Category();
        category1.setTitle("Category1");
        category1.setId(1L);

        Category category2 = new Category();
        category2.setTitle("Category2");
        category2.setId(2L);

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        List<CategoryResponseDTO> categories = categoryService.getAllCategories();

        assertEquals(2, categories.size());
        assertEquals("Category1", categories.get(0).getCategoryName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void getCategoryById_success() throws CategoryNotFoundException {
        Long categoryId = 1L;
        Category category = new Category();
        category.setTitle("Test Category");
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        CategoryResponseDTO fetchedCategory = categoryService.getCategory(categoryId);

        assertNotNull(fetchedCategory);
        assertEquals("Test Category", fetchedCategory.getCategoryName());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void updateCategory_success() throws CategoryNotFoundException {
        Long categoryId = 1L;
        CreateCategoryRequestDTO requestDTO = new CreateCategoryRequestDTO();
        requestDTO.setCategoryName("Updated Category");

        Category category = new Category();
        category.setTitle("Old Category");
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO updatedCategory = categoryService.updateCategory(requestDTO, categoryId);

        assertNotNull(updatedCategory);
        assertEquals("Updated Category", updatedCategory.getCategoryName());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void deleteCategory_success() throws CategoryNotFoundException {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    public void getTotalPriceForCategory_success() {
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);

        Product product1 = new Product();
        product1.setPrice(50.0);

        Product product2 = new Product();
        product2.setPrice(100.0);

        List<Product> products = List.of(product1, product2);
        category.setProducts(products);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        double totalPrice = categoryService.getTotalPriceForCategory(categoryId);

        assertEquals(150.0, totalPrice);

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void getTotalPriceForCategory_noProducts() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setProducts(new ArrayList<>());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        double totalPrice = categoryService.getTotalPriceForCategory(categoryId);

        assertEquals(0.0, totalPrice);
        verify(categoryRepository, times(1)).findById(categoryId);
    }
}
