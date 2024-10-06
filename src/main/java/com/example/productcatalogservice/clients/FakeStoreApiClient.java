package com.example.productcatalogservice.clients;

import com.example.productcatalogservice.dtos.FakeStoreProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class FakeStoreApiClient {

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    public List<FakeStoreProductDto> getAllProducts() {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        ResponseEntity<List<FakeStoreProductDto>> listFakeStoreDtoResponseEntity = restTemplate.exchange("https://fakestoreapi.com/products", HttpMethod.GET, null, new ParameterizedTypeReference<List<FakeStoreProductDto>>() {});
        ResponseEntity<FakeStoreProductDto[]> listFakeStoreDtoResponseEntity = requestForEntity("https://fakestoreapi.com/products",HttpMethod.GET,null,FakeStoreProductDto[].class);
        FakeStoreProductDto[] fakeStoreProductDtos = listFakeStoreDtoResponseEntity.getBody();
        List<FakeStoreProductDto> fakeStoreProductDtoList = new ArrayList<>();
        if (fakeStoreProductDtos != null) {
            fakeStoreProductDtoList.addAll(Arrays.asList(fakeStoreProductDtos));
        }
        return fakeStoreProductDtoList;
    }

    public FakeStoreProductDto  replaceProduct(Long id, FakeStoreProductDto fakeStoreProductDto) {
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity = this.requestForEntity("https://fakestoreapi.com/products/{id}",HttpMethod.PUT,fakeStoreProductDto, FakeStoreProductDto.class,id);
        FakeStoreProductDto responseFakeStoreProductDto = fakeStoreProductDtoResponseEntity.getBody();
        if(fakeStoreProductDtoResponseEntity.getStatusCode().equals(HttpStatusCode.valueOf(200)) && fakeStoreProductDto != null){
            return responseFakeStoreProductDto;
        }
        return null;
    }

    private  <T> ResponseEntity<T> requestForEntity(String url,HttpMethod httpMethod, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }

}
