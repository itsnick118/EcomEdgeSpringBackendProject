package com.library.productservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequestDTO {
    private String title;
    private double price;
    private String description;
    private String image;
    private Long categoryId;
}