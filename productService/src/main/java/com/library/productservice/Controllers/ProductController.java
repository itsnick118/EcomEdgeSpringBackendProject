package com.library.productservice.Controllers;

import com.library.productservice.Exceptions.ProductControllerSpecificException;
import com.library.productservice.Exceptions.ProductNotFoundException;
import com.library.productservice.Interface.IProductService;
import com.library.productservice.Models.Product;
import com.library.productservice.commons.AuthCommons;
import com.library.productservice.dto.CreateProductRequestDTO;
import com.library.productservice.dto.ProductResponseDTO;
import com.library.productservice.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

   private IProductService productService;
   private AuthCommons authCommons;
   private RestTemplate restTemplate;

    public ProductController(IProductService productService, AuthCommons authCommons, RestTemplate restTemplate) {
        this.productService = productService;
        this.authCommons = authCommons;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody CreateProductRequestDTO product){
        ProductResponseDTO savedProduct= productService.createProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.OK);
    }

    @GetMapping("/")
    public List<ProductResponseDTO> getAllProduct() throws ProductNotFoundException {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable("id") Long id) throws ProductNotFoundException {
      /*  UserDto userDto= authCommons.validateToken(token);

        if(userDto== null){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }*/
        ProductResponseDTO product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/{min}/{max}")
    public ResponseEntity getProductByPriceRange(@PathVariable("min") double minPrice, @PathVariable("max") double maxPrice){
        return ResponseEntity.ok(
                productService.getProducts(minPrice, maxPrice)
        );
    }

    @GetMapping("paged")
    public Page<ProductResponseDTO> findPagedProducts(@RequestParam("pageNumber") int pageNumber,
                                         @RequestParam("pageSize") int pageSize) throws ProductNotFoundException {
        return productService.findAllProducts(pageNumber,pageSize);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable("id") Long id, @RequestBody CreateProductRequestDTO productDetails) throws ProductNotFoundException {
        ProductResponseDTO product = productService.updateProduct(id, productDetails);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) throws ProductNotFoundException {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @ExceptionHandler(ProductControllerSpecificException.class)
    public ResponseEntity<Void> handleProductControllerSpecificException() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
