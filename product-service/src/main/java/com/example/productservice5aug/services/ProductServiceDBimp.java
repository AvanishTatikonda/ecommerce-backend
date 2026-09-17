package com.example.productservice5aug.services;

import com.example.productservice5aug.models.Category;
import com.example.productservice5aug.models.Product;
import com.example.productservice5aug.repositories.CategoryRepository;
import com.example.productservice5aug.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("dbProductService")
public class ProductServiceDBimp implements ProductService{
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    public ProductServiceDBimp(ProductRepository productRepository,CategoryRepository categoryrepository){
        this.productRepository=productRepository;
        this.categoryRepository=categoryrepository;
    }

    @Override
    public Product createProduct(Product product) {
        if (product.getCategory() != null) {
            String categoryTitle = product.getCategory().getTitle();
            Category categoryFromDatabase = categoryRepository.findByTitle(categoryTitle);

            if (categoryFromDatabase == null) {
                Category newCategory = new Category();
                newCategory.setTitle(categoryTitle);
                categoryFromDatabase = categoryRepository.save(newCategory);
            }
            product.setCategory(categoryFromDatabase);
        }

        return productRepository.save(product);
    }
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
    public Product updateProduct(Long id,Product product){
        Optional<Product> optionalProduct=productRepository.findById(id);
        if(optionalProduct.isEmpty()){
            return null;
        }
        Product exsistingProduct=optionalProduct.get();
        if(product.getTitle()!=null){
            exsistingProduct.setTitle(product.getTitle());
        }
        if(product.getDescription()!=null){
            exsistingProduct.setDescription(product.getDescription());
        }
        if(product.getImageUrl()!=null){
            exsistingProduct.setImageUrl((product.getImageUrl()));
        }
        if(product.getCategoryName()!=null){
            exsistingProduct.setCategoryName(product.getCategoryName());
        }
        if(product.getPrice()>0){
            exsistingProduct.setPrice(product.getPrice());
        }
        if (product.getCategory() != null) {
            String categoryTitle = product.getCategory().getTitle();

            Category categoryFromDatabase =
                    categoryRepository.findByTitle(categoryTitle);

            if (categoryFromDatabase == null) {
                Category newCategory = new Category();
                newCategory.setTitle(categoryTitle);
                categoryFromDatabase = categoryRepository.save(newCategory);
            }

            exsistingProduct.setCategory(categoryFromDatabase);
        }
        return productRepository.save(exsistingProduct);
    }
    public Product getSingleProduct(Long id){
        return productRepository.findById(id).orElse(null);
    }
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }
}
