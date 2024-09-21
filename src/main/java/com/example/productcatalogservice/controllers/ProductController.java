package com.example.productcatalogservice.controllers;

import com.example.productcatalogservice.models.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return null;
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable("id") Long productId) {
        Product product = new Product();
        product.setId(productId);
        product.setTitle("Pixel 9 Fold");
        product.setDescription("This is a pixel 9 Fold");
        product.setAmount(Double.valueOf(900000));
        return product;
    }

    @PostMapping("/product")
    public Product addProduct(@RequestBody Product product) {
        return null;
    }
}
