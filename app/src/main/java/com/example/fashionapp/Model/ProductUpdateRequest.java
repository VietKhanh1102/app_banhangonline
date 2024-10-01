package com.example.fashionapp.Model;

public class ProductUpdateRequest {
    private int product_id;
    private int quantity;

    // Các phương thức getter và setter
    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
