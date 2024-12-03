package com.library.productservice.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private Long productId;
    private String productTitle;
    private double price;
    private int quantity;
}
