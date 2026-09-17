package com.example.productservice5aug.services;

import com.example.productservice5aug.dtos.CreateProductRequestDto;
import com.example.productservice5aug.dtos.FakeStoreCreateProductRequestDto;
import com.example.productservice5aug.dtos.FakeStoreCreateProductResponseDto;
import com.example.productservice5aug.models.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("fakestoreProductService")
public class ProductServiceFakeStroreimp implements ProductService {
    private RestTemplate restTemplate;

    public ProductServiceFakeStroreimp(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }
    public Product createProduct(Product product){
        FakeStoreCreateProductRequestDto request=new FakeStoreCreateProductRequestDto();
        request.setCategory(product.getCategoryName());
        request.setTitle(product.getTitle());
        request.setImage(product.getImageUrl());
        request.setPrice(product.getPrice());
        request.setDescription(product.getDescription());

        FakeStoreCreateProductResponseDto response=restTemplate.postForObject(
                "https://fakestoreapi.com/products",
                request,
                FakeStoreCreateProductResponseDto.class

        );
        Product product1=new Product();
        product1.setId(response.getId());
        product1.setTitle(response.getTitle());
        product1.setDescription(response.getDescription());
       product1.setCategoryName(response.getCategory());
        product1.setImageUrl(response.getImage());
        product1.setPrice(response.getPrice());
        return product1;
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreCreateProductResponseDto[] response=restTemplate.getForObject(
                "https://fakestoreapi.com/products",
                FakeStoreCreateProductResponseDto[].class
        );
        List<Product> products=new ArrayList<>();
        for(FakeStoreCreateProductResponseDto dto : response){
            Product product =new Product();
            product.setId(dto.getId());
            product.setTitle(dto.getTitle());
            product.setDescription(dto.getDescription());
            product.setCategoryName(dto.getCategory());
            product.setImageUrl(dto.getImage());
            product.setPrice(dto.getPrice());
            products.add(product);
        }
        return products;
    }
    @Override
    public  Product updateProduct(Long id,Product product){
        FakeStoreCreateProductRequestDto request=new FakeStoreCreateProductRequestDto();
       request.setCategory(product.getCategoryName());
        request.setTitle(product.getTitle());
        request.setImage(product.getImageUrl());
        request.setPrice(product.getPrice());
        request.setDescription(product.getDescription());

        FakeStoreCreateProductResponseDto response=restTemplate.exchange(
                "https://fakestoreapi.com/products/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                FakeStoreCreateProductResponseDto.class
        ).getBody();
        Product updatesProduct =new Product();
        updatesProduct.setId((response.getId()));
        updatesProduct.setPrice((response.getPrice()));
        updatesProduct.setDescription(response.getDescription());
        updatesProduct.setImageUrl(response.getImage());
        updatesProduct.setTitle(response.getTitle());
        updatesProduct.setCategoryName(response.getCategory());
        return updatesProduct;
    }

    public Product getSingleProduct(Long id){
        FakeStoreCreateProductResponseDto responseDto = restTemplate.getForObject(
                "https://fakestoreapi.com/products/"+id,
                FakeStoreCreateProductResponseDto.class
        );
        Product product =new Product();
        product.setId(responseDto.getId());
        product.setPrice(responseDto.getPrice());;
        product.setTitle(responseDto.getTitle());
        product.setDescription(responseDto.getDescription());
        product.setImageUrl(responseDto.getImage());
        product.setCategoryName(responseDto.getCategory());

        return product;
    }

    public void deleteProduct(Long id){
        restTemplate.delete(
        "https://fakestoreapi.com/products/"+id
                );
    }
}
