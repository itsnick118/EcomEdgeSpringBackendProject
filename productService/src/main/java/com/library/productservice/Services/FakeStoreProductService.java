package com.library.productservice.Services;

import com.library.productservice.Exceptions.ProductNotFoundException;
import com.library.productservice.Exceptions.ProductsNotFoundException;
import com.library.productservice.Interface.IProductService;
import com.library.productservice.Models.Product;
import com.library.productservice.dto.CreateProductRequestDTO;
import com.library.productservice.dto.FakeStoreProductDto;
import com.library.productservice.dto.ProductResponseDTO;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
//@Primary
public class FakeStoreProductService implements IProductService {

    private RestTemplate restTemplate;
    private RedisTemplate<String, Object> redisTemplate;

    public FakeStoreProductService(RestTemplate restTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ProductResponseDTO getProductById(Long id) throws ProductNotFoundException {
        Product product = (Product) redisTemplate.opsForHash().get("PRODUCTS", "PRODUCTS_" + id);

        if (product != null) {
            return ProductResponseDTO.from(product);
        }
        FakeStoreProductDto fakeStoreProductDto =
                restTemplate.getForObject("https://fakestoreapi.com/products/" + id,
                        FakeStoreProductDto.class);
        if (fakeStoreProductDto == null) {
            throw new ProductNotFoundException("Product with id" + id, id);
        }


        redisTemplate.opsForHash().put("PRODUCTS", "PRODUCTS_" + id, ProductResponseDTO.from(product));

        return ProductResponseDTO.from(product);
    }


    @Override
    public List<ProductResponseDTO> getAllProducts() throws ProductsNotFoundException{

        FakeStoreProductDto[] fakeStoreProductDtos =
                restTemplate.getForObject("https://fakestoreapi.com/products/",
                        FakeStoreProductDto[].class);
        if (fakeStoreProductDtos == null) {
            throw new ProductsNotFoundException();
        }
        List<ProductResponseDTO> productResponseDTO = new ArrayList<>();
        for (FakeStoreProductDto fakeStoreProductDto : fakeStoreProductDtos) {
            productResponseDTO.add(convertFakeStoreDtoToProductResponseDTO(fakeStoreProductDto));
        }
        return productResponseDTO;
    }

    @Override
    public List<ProductResponseDTO> getProducts(double minPrice, double maxPrice) {
        return List.of();
    }

    @Override
    public Page<ProductResponseDTO> findAllProducts(int pageNumber, int pageSize) throws ProductsNotFoundException{
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        FakeStoreProductDto[] fakeStoreProductDtos = restTemplate.getForObject(
                "https://fakestoreapi.com/products", FakeStoreProductDto[].class);

        if (fakeStoreProductDtos == null) {
            throw new ProductsNotFoundException();
        }
        List<ProductResponseDTO> productResponseDTO = new ArrayList<>();
        for (FakeStoreProductDto dto : fakeStoreProductDtos) {
            productResponseDTO.add(convertFakeStoreDtoToProductResponseDTO(dto));
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productResponseDTO.size());
        return new PageImpl<>(productResponseDTO.subList(start, end), pageable, productResponseDTO.size());
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, CreateProductRequestDTO product) throws ProductsNotFoundException {
        FakeStoreProductDto fakeStoreProductDto =
                restTemplate.getForObject("https://fakestoreapi.com/products/" + id,
                        FakeStoreProductDto.class);
        if (fakeStoreProductDto == null) {
            throw new ProductsNotFoundException();
        }
        fakeStoreProductDto.setTitle(product.getTitle());
        fakeStoreProductDto.setImage(product.getImage());
        fakeStoreProductDto.setDescription(product.getDescription());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setCategory(fakeStoreProductDto.getCategory());
        HttpEntity<FakeStoreProductDto> requestEntity = new HttpEntity<>(fakeStoreProductDto);
        ResponseEntity<FakeStoreProductDto> responseEntity = restTemplate.exchange(
                "https://fakestoreapi.com/products/" + id,
                HttpMethod.PUT,
                requestEntity,
                FakeStoreProductDto.class
        );


        FakeStoreProductDto response = responseEntity.getBody();
        return convertFakeStoreDtoToProductResponseDTO(response);
    }

    @Override
    public ProductResponseDTO createProduct(CreateProductRequestDTO product) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setTitle(product.getTitle());
        fakeStoreProductDto.setImage(product.getImage());
        fakeStoreProductDto.setDescription(product.getDescription());
        fakeStoreProductDto.setPrice(product.getPrice());

        HttpEntity<FakeStoreProductDto> requestEntity = new HttpEntity<>(fakeStoreProductDto);
        ResponseEntity<FakeStoreProductDto> responseEntity = restTemplate.postForEntity(
                "https://fakestoreapi.com/products",
                requestEntity,
                FakeStoreProductDto.class
        );

        FakeStoreProductDto response = responseEntity.getBody();
        return convertFakeStoreDtoToProductResponseDTO(response);
    }

    @Override
    public String deleteProduct(Long id) throws ProductNotFoundException{
        FakeStoreProductDto fakeStoreProductDto =
                restTemplate.getForObject("https://fakestoreapi.com/products/" + id,
                        FakeStoreProductDto.class);
        if (fakeStoreProductDto == null) {
            throw new ProductNotFoundException("Product with id" + id, id);
        }
        restTemplate.delete("https://fakestoreapi.com/products/" + id);
        return "Product with ID " + id + " has been successfully deleted.";
    }

    private ProductResponseDTO convertFakeStoreDtoToProductResponseDTO(FakeStoreProductDto dto) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(dto.getId());
        productResponseDTO.setTitle(dto.getTitle());
        productResponseDTO.setPrice(dto.getPrice());
        productResponseDTO.setDescription(dto.getDescription());
        productResponseDTO.setImage(dto.getImage());
        productResponseDTO.setCategory(dto.getCategory());
        return productResponseDTO;
    }
}
