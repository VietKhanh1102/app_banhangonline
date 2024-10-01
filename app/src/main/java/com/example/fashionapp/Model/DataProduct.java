package com.example.fashionapp.Model;

import java.util.List;

public class DataProduct {

    private List<Product> data;

    public DataProduct() {
    }

    public DataProduct(List<Product> data) {
        this.data = data;
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
