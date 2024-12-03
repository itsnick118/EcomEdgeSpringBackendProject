package com.library.productservice.Repostories;

import com.library.productservice.Models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product save(Product product);

    Optional<Product> findById(Long id);

    Page<Product> findAll(Pageable pageable);

    void deleteById(Long id);

    Product findProductByTitle(String title);
    Product findFirstProductByTitle(String title);
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
}
