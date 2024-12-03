package com.library.productservice.dto;

import com.library.productservice.Models.Product;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProductResponseDTO implements Serializable {
    private Long id;
    private String title;
    private double price;
    private String category;
    private String description;
    private String image;

    public static ProductResponseDTO from(Product product){
        if(product== null){
            return null;
        }
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(product.getId());
        productResponseDTO.setTitle(product.getTitle());
        productResponseDTO.setPrice(product.getPrice());
        productResponseDTO.setImage(product.getImage());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setCategory(product.getCategory().getTitle());
        return productResponseDTO;
    }
}