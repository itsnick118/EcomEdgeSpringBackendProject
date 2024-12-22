package com.library.productservice.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.library.productservice.Interface.ICategoryService;
import com.library.productservice.dto.CategoryResponseDTO;
import com.library.productservice.dto.CreateCategoryRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class CategoryControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private ICategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    public void getAllCategories_success() throws Exception {
        CategoryResponseDTO category1 = new CategoryResponseDTO();
        category1.setCategoryId(1L);
        category1.setCategoryName("Category1");

        CategoryResponseDTO category2 = new CategoryResponseDTO();
        category2.setCategoryId(2L);
        category2.setCategoryName("Category2");

        List<CategoryResponseDTO> categories = Arrays.asList(category1, category2);

        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(MockMvcRequestBuilders.get("/category/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].categoryName").value("Category1"))
                .andExpect(jsonPath("$[1].categoryName").value("Category2"));
    }

    @Test
    public void getCategoryById_success() throws Exception {
        CategoryResponseDTO category = new CategoryResponseDTO();
        category.setCategoryId(1L);
        category.setCategoryName("Category1");

        when(categoryService.getCategory(1L)).thenReturn(category);

        mockMvc.perform(MockMvcRequestBuilders.get("/category/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("Category1"));
    }

    @Test
    public void createCategory_success() throws Exception {
        CreateCategoryRequestDTO createCategoryRequest = new CreateCategoryRequestDTO();
        createCategoryRequest.setCategoryName("NewCategory");

        CategoryResponseDTO categoryResponse = new CategoryResponseDTO();
        categoryResponse.setCategoryId(1L);
        categoryResponse.setCategoryName("NewCategory");

        when(categoryService.createCategory(ArgumentMatchers.any(CreateCategoryRequestDTO.class)))
                .thenReturn(categoryResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/category/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCategoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.categoryName").value("NewCategory"));
    }

    @Test
    public void updateCategory_success() throws Exception {
        CreateCategoryRequestDTO updateRequest = new CreateCategoryRequestDTO();
        updateRequest.setCategoryName("UpdatedCategory");

        CategoryResponseDTO updatedCategory = new CategoryResponseDTO();
        updatedCategory.setCategoryId(1L);
        updatedCategory.setCategoryName("UpdatedCategory");

        when(categoryService.updateCategory(ArgumentMatchers.any(CreateCategoryRequestDTO.class), ArgumentMatchers.eq(1L)))
                .thenReturn(updatedCategory);

        mockMvc.perform(MockMvcRequestBuilders.put("/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("UpdatedCategory"));
    }

    @Test
    public void deleteCategory_success() throws Exception {
        Long categoryId = 1L;
        String successMessage = "Category with ID " + categoryId + " has been successfully deleted.";

        when(categoryService.deleteCategory(categoryId)).thenReturn(successMessage);

        mockMvc.perform(MockMvcRequestBuilders.delete("/category/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));
    }

    @Test
    public void getTotalPriceForAllProducts_success() throws Exception {
        Long categoryId = 1L;
        Double totalPrice = 100.0;

        when(categoryService.getTotalPriceForCategory(categoryId)).thenReturn(totalPrice);

        mockMvc.perform(MockMvcRequestBuilders.get("/category/totalPrice/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("100.0"));
    }
}