package com.example.productservice5aug.controllers;

import com.example.productservice5aug.dtos.CreateProductRequestDto;
import com.example.productservice5aug.dtos.CreateProductResponseDto;
import com.example.productservice5aug.dtos.UpdateProductRequestDto;
import com.example.productservice5aug.models.Product;
import com.example.productservice5aug.services.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;
    public ProductController(@Qualifier("dbProductService")ProductService productService){
        this.productService=productService;
    }

    @PostMapping("")
    public CreateProductResponseDto createProduct(@RequestBody CreateProductRequestDto createProductRequestDto){
        Product product=productService.createProduct(
                createProductRequestDto.toProduct()
        );

        return CreateProductResponseDto.fromProduct(product);
       //return "this product is priced at :" +createProductRequestDto.getPrice();
    }
    @GetMapping("")
     public List<CreateProductResponseDto> getAllProducts(){
        List<Product> products=productService.getAllProducts();
//        throw new RuntimeException();
        List<CreateProductResponseDto> responseDtos=new ArrayList<>();
        for(Product product:products){
            responseDtos.add(CreateProductResponseDto.fromProduct(product));
        }
        return responseDtos;
    }
    @GetMapping("{id}")
    public CreateProductResponseDto getSingleProduct(@PathVariable Long id){
        Product product=productService.getSingleProduct(id);
        return CreateProductResponseDto.fromProduct(product);
    }
    @PutMapping("{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @RequestBody UpdateProductRequestDto requestDto){
        Product product =productService.updateProduct(
                id,
                requestDto.toProduct()
        );
        return product;
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        productService.deleteProduct(id);
    }

}
