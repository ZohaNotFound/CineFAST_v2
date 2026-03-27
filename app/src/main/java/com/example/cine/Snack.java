package com.example.cine;

public class Snack {
    private String name;
    private float price;
    private int imageResId;
    private int quantity;

    public Snack(String name, float price, int imageResId) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = 0;
    }

    public String getName() { return name; }
    public float getPrice() { return price; }
    public int getImageResId() { return imageResId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}