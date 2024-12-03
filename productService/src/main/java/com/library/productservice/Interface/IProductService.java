package com.library.productservice.Interface;

import com.library.productservice.Exceptions.ProductNotFoundException;
import com.library.productservice.Exceptions.ProductsNotFoundException;
import com.library.productservice.Models.Product;
import com.library.productservice.dto.CreateProductRequestDTO;
import com.library.productservice.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {

    ProductResponseDTO createProduct(CreateProductRequestDTO productRequestDTO);

    List<ProductResponseDTO> getAllProducts() throws ProductsNotFoundException;

    List<ProductResponseDTO> getProducts(double minPrice, double maxPrice);

    ProductResponseDTO getProductById(Long id) throws ProductNotFoundException;

    Page<ProductResponseDTO> findAllProducts(int pageNumber, int pageSize) throws ProductsNotFoundException;

    ProductResponseDTO updateProduct(Long id, CreateProductRequestDTO product)throws ProductNotFoundException;

    String deleteProduct(Long id) throws ProductNotFoundException;

}
