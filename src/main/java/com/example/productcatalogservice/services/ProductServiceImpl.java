package com.example.productcatalogservice.services;

import com.example.productcatalogservice.clients.FakeStoreApiClient;
import com.example.productcatalogservice.dtos.FakeStoreProductDto;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private FakeStoreApiClient fakeStoreApiClient;

//    @Override
//    public List<Product> getAllProducts() {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        ResponseEntity<List<FakeStoreProductDto>> listFakeStoreDtoResponseEntity = restTemplate.exchange("https://fakestoreapi.com/products", HttpMethod.GET, null, new ParameterizedTypeReference<List<FakeStoreProductDto>>() {});
//        List<FakeStoreProductDto> fakeStoreProductDtos = listFakeStoreDtoResponseEntity.getBody();
//        List<Product> products = new ArrayList<>();
//        if (fakeStoreProductDtos != null) {
//            for (FakeStoreProductDto fakeStoreProductDto : fakeStoreProductDtos) {
//                products.add(from(fakeStoreProductDto));
//            }
//        }
//        return products;
//    }

    @Override
    public List<Product> getAllProducts() {
        List<FakeStoreProductDto> fakeStoreProductDtos = fakeStoreApiClient.getAllProducts();
        List<Product> products = new ArrayList<>();
        if (fakeStoreProductDtos != null) {
            for (FakeStoreProductDto fakeStoreProductDto : fakeStoreProductDtos) {
                products.add(from(fakeStoreProductDto));
            }
        }
        return products;
    }

    @Override
    public Product getProductById(Long id) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity = restTemplate.getForEntity("https://fakestoreapi.com/products/{id}", FakeStoreProductDto.class,id);
        FakeStoreProductDto fakeStoreProductDto = fakeStoreProductDtoResponseEntity.getBody();
        if (fakeStoreProductDtoResponseEntity.getStatusCode().equals(HttpStatusCode.valueOf(200)) && fakeStoreProductDto != null) {
            return from(fakeStoreProductDto);
        }
        return null;
    }

    @Override
    public Product createProduct(Product product) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        FakeStoreProductDto fakeStoreProductDto = from(product);
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity = restTemplate.postForEntity("https://fakestoreapi.com/products",fakeStoreProductDto, FakeStoreProductDto.class);
        if(fakeStoreProductDtoResponseEntity.getStatusCode().equals(HttpStatusCode.valueOf(200)) && fakeStoreProductDtoResponseEntity.getBody() != null)
            return from(fakeStoreProductDtoResponseEntity.getBody());
        else
            return null;
    }

    @Override
    public Product replaceProduct(Long id, Product product) {
        FakeStoreProductDto fakeStoreProductDto = fakeStoreApiClient.replaceProduct(id,from(product));
        return from(fakeStoreProductDto);
    }

    private Product from(FakeStoreProductDto fakeStoreProductDto) {
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setAmount(fakeStoreProductDto.getPrice());
        product.setTitle(fakeStoreProductDto.getTitle());
        product.setImageUrl(fakeStoreProductDto.getImage());
        product.setDescription(fakeStoreProductDto.getDescription());
        Category category = new Category();
        category.setCategoryName(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        return product;

    }

    private FakeStoreProductDto from(Product product) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setId(product.getId());
        fakeStoreProductDto.setPrice(product.getAmount());
        fakeStoreProductDto.setTitle(product.getTitle());
        fakeStoreProductDto.setImage(product.getImageUrl());
        fakeStoreProductDto.setDescription(product.getDescription());
        fakeStoreProductDto.setCategory(product.getCategory().getCategoryName());
        return fakeStoreProductDto;

    }

}
