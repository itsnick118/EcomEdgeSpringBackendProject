package com.library.productservice.Services;

import com.library.productservice.Exceptions.CategoryNotFoundException;
import com.library.productservice.Exceptions.ProductNotFoundException;
import com.library.productservice.Interface.ICategoryService;
import com.library.productservice.Models.Category;
import com.library.productservice.Models.Product;
import com.library.productservice.Repostories.CategoryRepository;
import com.library.productservice.Repostories.ProductRepository;
import com.library.productservice.dto.CategoryResponseDTO;
import com.library.productservice.dto.CreateCategoryRequestDTO;
import com.library.productservice.dto.ProductResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public CategoryResponseDTO getCategory(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            return CategoryResponseDTO.from(category);
        }
        throw new CategoryNotFoundException("Category with id" + categoryId, categoryId);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponseDTO> categoryResponseDTOs = new ArrayList<>();
        for (Category category : categories) {
            categoryResponseDTOs.add(CategoryResponseDTO.from(category));
        }
        return categoryResponseDTOs;
    }

    @Override
    public CategoryResponseDTO createCategory(CreateCategoryRequestDTO categoryRequestDTO) {
        Category category = Category.from(categoryRequestDTO);
        category = categoryRepository.save(category);
        return CategoryResponseDTO.from(category);
    }

    @Override
    public CategoryResponseDTO updateCategory(CreateCategoryRequestDTO categoryRequestDTO, Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setTitle(categoryRequestDTO.getCategoryName());
            categoryRepository.save(category);
            return CategoryResponseDTO.from(category);
        }
        throw new CategoryNotFoundException("Category with id" + categoryId, categoryId);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            categoryRepository.delete(category);
            return "Category with ID " + categoryId + " has been successfully deleted.";
        }
        throw new CategoryNotFoundException("Category with ID " + categoryId + " not found.", categoryId);
    }

    @Override
    public double getTotalPriceForCategory(Long categoryId) {
        List<Product> productResponseDTOS = productRepository.findAll();
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException("Category with id " + categoryId + " not found", categoryId)
        );
        if (category.getProducts().isEmpty()) {
            return 0;
        } else {
            double sum = 0;
            for (Product p : category.getProducts()) {
                sum = sum + p.getPrice();
            }
            return sum;
        }
    }
}