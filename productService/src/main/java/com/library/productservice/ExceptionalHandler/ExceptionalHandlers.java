package com.library.productservice.ExceptionalHandler;

import com.library.productservice.Exceptions.CategoryNotFoundException;
import com.library.productservice.Exceptions.OrderNotFoundException;
import com.library.productservice.Exceptions.ProductNotFoundException;
import com.library.productservice.Exceptions.ProductsNotFoundException;
import com.library.productservice.dto.CategoryNotFoundDto;
import com.library.productservice.dto.ExceptionDto;
import com.library.productservice.dto.OrderNotFoundDto;
import com.library.productservice.dto.ProductNotFoundDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionalHandlers {

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<ExceptionDto> handleArithmeticException() {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage("Arithmetic Exception Occurred");
        exceptionDto.setResolution("Please check the arithmetic operation");

        return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ResponseEntity<Void> handleArrayOutOfBoundException() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ProductNotFoundDto> handleProductNotFoundException(ProductNotFoundException ex) {
        ProductNotFoundDto productNotFoundDto = new ProductNotFoundDto();
        productNotFoundDto.setMessage("Product with ID " + ex.getId() + " not found");
        return new ResponseEntity<>(productNotFoundDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductsNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleProductsNotFoundException(ProductsNotFoundException ex) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage("No products found in the catalog");
        exceptionDto.setResolution("Check if products are available in the inventory");

        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<CategoryNotFoundDto> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        CategoryNotFoundDto categoryNotFoundDto = new CategoryNotFoundDto();
        categoryNotFoundDto.setMessage("Category with ID " + ex.getId() + " not found");
        return new ResponseEntity<>(categoryNotFoundDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<OrderNotFoundDto> handleCategoryNotFoundException(OrderNotFoundException ex) {
        OrderNotFoundDto orderNotFoundDto = new OrderNotFoundDto();
        orderNotFoundDto.setMessage("Order with ID " + ex.getId() + " not found");
        return new ResponseEntity<>(orderNotFoundDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionDto> handleNullPointerException(NullPointerException ex) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage("A null pointer exception occurred");
        exceptionDto.setResolution("Ensure all required fields are initialized");

        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleGlobalException(Exception ex, WebRequest request) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage("An error occurred: " + ex.getMessage());
        exceptionDto.setResolution("Details: " + request.getDescription(false));

        return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
