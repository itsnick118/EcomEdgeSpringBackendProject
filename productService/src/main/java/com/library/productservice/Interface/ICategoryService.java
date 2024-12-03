package com.library.productservice.Interface;

import com.library.productservice.dto.CategoryResponseDTO;
import com.library.productservice.dto.CreateCategoryRequestDTO;

import java.util.List;

public interface ICategoryService {
    CategoryResponseDTO getCategory(Long categoryId);
    List<CategoryResponseDTO> getAllCategories();
    CategoryResponseDTO createCategory(CreateCategoryRequestDTO categoryRequestDTO);
    CategoryResponseDTO updateCategory(CreateCategoryRequestDTO categoryRequestDTO, Long categoryId);
    String deleteCategory(Long categoryId);
    double getTotalPriceForCategory(Long categoryId);
}