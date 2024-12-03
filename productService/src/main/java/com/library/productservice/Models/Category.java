package com.library.productservice.Models;

import com.library.productservice.dto.CreateCategoryRequestDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Category extends BaseModel{
    private String title;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Product> products;

    public static Category from(CreateCategoryRequestDTO createCategoryRequestDTO){
        Category category = new Category();
        category.setTitle(createCategoryRequestDTO.getCategoryName());
        return category;
    }
}
