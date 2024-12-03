package com.library.productservice.Exceptions;

public class ProductsNotFoundException extends RuntimeException {
    public ProductsNotFoundException() {
        super("No products found in the catalog");
    }
}

