package com.library.productservice.Repostories;

import com.library.productservice.Models.Category;
import com.library.productservice.Models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category save(Category category);

    Optional<Category> findById(Long id);

    Page<Category> findAll(Pageable pageable);

    void deleteById(Long id);
}
