package com.library.productservice.Models;

import com.library.productservice.dto.CreateProductRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product extends BaseModel{
    private String title;
    private double price;
    private String description;
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    public static Product from(CreateProductRequestDTO productRequestDTO){
        Product product = new Product();
        product.setTitle(productRequestDTO.getTitle());
        product.setPrice(productRequestDTO.getPrice());
        product.setImage(productRequestDTO.getImage());
        product.setDescription(productRequestDTO.getDescription());
        return product;
    }
}
