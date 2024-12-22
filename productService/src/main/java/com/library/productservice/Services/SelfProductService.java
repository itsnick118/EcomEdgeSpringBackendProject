package com.library.productservice.Services;

import com.library.productservice.Exceptions.CategoryNotFoundException;
import com.library.productservice.Exceptions.ProductNotFoundException;
import com.library.productservice.Exceptions.ProductsNotFoundException;
import com.library.productservice.Interface.IProductService;
import com.library.productservice.Models.Category;
import com.library.productservice.Models.Product;
import com.library.productservice.Repostories.CategoryRepository;
import com.library.productservice.Repostories.ProductRepository;
import com.library.productservice.dto.CreateProductRequestDTO;
import com.library.productservice.dto.ProductResponseDTO;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class SelfProductService implements IProductService {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public SelfProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductResponseDTO createProduct(CreateProductRequestDTO productRequestDTO) {
        Category savedCategory = categoryRepository.findById(productRequestDTO.getCategoryId()).orElseThrow(
                () -> new CategoryNotFoundException("Category not found for id : " + productRequestDTO.getCategoryId(), productRequestDTO.getCategoryId()));

        Product product = Product.from(productRequestDTO);
        product.setCategory(savedCategory);

        Product savedProduct = productRepository.save(product);

        return ProductResponseDTO.from(savedProduct);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() throws ProductsNotFoundException {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ProductsNotFoundException();
        }
        List<ProductResponseDTO> productResponseDTOs = new ArrayList<>();
        for (Product product : products) {
            productResponseDTOs.add(ProductResponseDTO.from(product));
        }
        return productResponseDTOs;
    }

    @Override
    public ProductResponseDTO getProductById(Long id) throws ProductNotFoundException {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return ProductResponseDTO.from(product);
        }
        throw new ProductNotFoundException("Product with id" + id, id);
    }

    @Override
    public Page<ProductResponseDTO> findAllProducts(int pageNumber, int pageSize) throws ProductsNotFoundException {
        Page<Product> productsPage = productRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by("price").descending()));
        if (productsPage.isEmpty()) {
            throw new ProductsNotFoundException();
        }
        return productsPage.map(ProductResponseDTO::from);
    }

    @Override
    public List<ProductResponseDTO> getProducts(double minPrice, double maxPrice) throws ProductsNotFoundException{
        List<ProductResponseDTO> productResponseDTOs = new ArrayList<>();
        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        if (products.isEmpty()) {
            throw new ProductsNotFoundException();
        }
        for (Product product : products) {
            productResponseDTOs.add(ProductResponseDTO.from(product));
        }
        return productResponseDTOs;
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, CreateProductRequestDTO product) throws ProductNotFoundException {
        Optional<Product> fetchProductOptional = productRepository.findById(id);
        if (fetchProductOptional.isPresent()) {
            Product fetchProduct = fetchProductOptional.get();
            fetchProduct.setTitle(product.getTitle());
            fetchProduct.setDescription(product.getDescription());
            fetchProduct.setCategory(fetchProduct.getCategory());
            fetchProduct.setPrice(product.getPrice());
            fetchProduct.setImage(product.getImage());
            productRepository.save(fetchProduct);
            return ProductResponseDTO.from(fetchProduct);
        }
        throw new ProductNotFoundException("Product with id" + id, id);
    }

    @Override
    public String deleteProduct(Long id) throws ProductNotFoundException {
        Optional<Product> fetchProductOptional = productRepository.findById(id);
        if (fetchProductOptional.isPresent()) {
            Product fetchProduct = fetchProductOptional.get();
            productRepository.delete(fetchProduct);
            return "Product with ID " + id + " has been successfully deleted.";
        }
        throw new ProductNotFoundException("Product with id" + id + " not found.", id);
    }

}
