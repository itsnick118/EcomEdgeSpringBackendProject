package com.library.productservice.Repostories;

import com.library.productservice.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
