package com.library.productservice.dto;

import com.library.productservice.Models.Category;
import com.library.productservice.Models.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryResponseDTO {
    private long categoryId;
    private String categoryName;
    private List<ProductResponseDTO> products;

    public static CategoryResponseDTO from(Category category){
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setCategoryId(category.getId());
        categoryResponseDTO.setCategoryName(category.getTitle());

        List<ProductResponseDTO> productResponseDTOs = new ArrayList<>();
        if(!(category.getProducts() == null || category.getProducts().isEmpty())) {
            for (Product product : category.getProducts()) {
                productResponseDTOs.add(ProductResponseDTO.from(product));
            }
        }
        categoryResponseDTO.setProducts(productResponseDTOs);
        return categoryResponseDTO;
    }
}
