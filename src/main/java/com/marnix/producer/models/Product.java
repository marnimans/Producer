package com.marnix.producer.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id", scope = Product.class)
public class Product {
    public String name;
    public String price;
    public String description;

    public Product() {

    }

    public Product(String name, String price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getProductName() {
        return name;
    }

    public void setProductName(String productName) {
        this.name = productName;
    }

    public String getProductPrice() {
        return price;
    }

    public void setProductPrice(String productPrice) {
        this.price = productPrice;
    }

    public String getProductDescription() {
        return description;
    }

    public void setProductDescription(String productDescription) {
        this.description = productDescription;
    }
}