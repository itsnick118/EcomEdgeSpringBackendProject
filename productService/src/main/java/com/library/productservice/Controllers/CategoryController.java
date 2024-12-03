package com.library.productservice.Controllers;

import com.library.productservice.Interface.ICategoryService;
import com.library.productservice.Models.Category;
import com.library.productservice.dto.CategoryResponseDTO;
import com.library.productservice.dto.CreateCategoryRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable("id")Long categoryId){
        return ResponseEntity.ok(categoryService.getCategory(categoryId));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CreateCategoryRequestDTO createCategoryRequestDTO){
        return ResponseEntity.ok(categoryService.createCategory(createCategoryRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable("id") Long categoryId, @RequestBody CreateCategoryRequestDTO createCategoryRequestDTO) throws Exception {
        return ResponseEntity.ok(categoryService.updateCategory(createCategoryRequestDTO, categoryId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long categoryId){
        return ResponseEntity.ok(categoryService.deleteCategory(categoryId));
    }

    @GetMapping("/totalPrice/{categoryId}")
    public ResponseEntity<Double> getTotalPriceForAllProducts(@PathVariable("categoryId") Long categoryId){
        return ResponseEntity.ok(categoryService.getTotalPriceForCategory(categoryId));
    }
}
