package com.example.productservice5aug.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category extends BaseModel{
     private String title;
     @OneToMany(mappedBy = "category")
     @JsonIgnore
     private List<Product> products;
     @ManyToMany
     private List<Product> featuredProducts;
}
