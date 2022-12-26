package com.mongo.crud.example.mongo_crud_demo.request;


public class BookRequest {
    private String name;
    private String quantity;
    private String description;
    private double price;

    public BookRequest(String name, String quantity, String description, double price) {
        this.name = name;
        this.quantity = quantity;
        this.description = description;
        this.price = price;
    }

    public BookRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
