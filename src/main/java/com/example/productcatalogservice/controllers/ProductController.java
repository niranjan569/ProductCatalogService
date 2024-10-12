package com.example.productcatalogservice.controllers;

import com.example.productcatalogservice.dtos.CategoryDto;
import com.example.productcatalogservice.dtos.ProductDto;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public List<ProductDto> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(from(product));
        }
        return productDtos;
    }

    @GetMapping("/products/{id}")
//    @RequestMapping(value = "/products/{id}",method = RequestMethod.GET)
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId) {
//        Product product = new Product();
//        product.setId(productId);
//        product.setTitle("Pixel 9 Fold");
//        product.setDescription("This is a pixel 9 Fold");
//        product.setAmount(Double.valueOf(900000));
//        return product;
        if(productId < 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product id");
        Product product = productService.getProductById(productId);
        if(product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Product found with id " + productId);
        }
        return new ResponseEntity<>(from(product), HttpStatus.OK);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        try {
            if(productDto == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body cannot be empty");
            }
            if(productDto.getId() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product Id cannot be null");
            Product product = from(productDto);
            Product ceatedProduct = productService.createProduct(product);
            return new ResponseEntity<>(from(ceatedProduct),HttpStatus.OK);
        }catch (ResponseStatusException e) {
            throw e;
//            System.out.println(e.getMessage());
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> replaceProduct(@PathVariable("id") Long id, @RequestBody ProductDto productDto) {
        Product product = productService.replaceProduct(id, from(productDto));
        return new ResponseEntity<>(from(product), HttpStatus.OK);
    }

    private ProductDto from(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setAmount(product.getAmount());
        productDto.setDescription(product.getDescription());
        productDto.setImageUrl(product.getImageUrl());
        productDto.setTitle(product.getTitle());
        if(product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setCategoryName(product.getCategory().getCategoryName());
            categoryDto.setCategoryDescription(product.getCategory().getCategoryDescription());
            productDto.setCategory(categoryDto);
        }
        return productDto;
    }

    private Product from(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setAmount(productDto.getAmount());
        product.setDescription(productDto.getDescription());
        product.setImageUrl(productDto.getImageUrl());
        product.setTitle(productDto.getTitle());
        if(productDto.getCategory() != null) {
            Category category = new Category();
            category.setId(productDto.getCategory().getId());
            category.setCategoryName(productDto.getCategory().getCategoryName());
            category.setCategoryDescription(productDto.getCategory().getCategoryDescription());
            product.setCategory(category);
        }
        return product;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleException(ResponseStatusException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getStatusCode());
    }
}
