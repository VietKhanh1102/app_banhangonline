package com.example.fashionapp.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "productFavorite")
public class Product implements Serializable {
    private String name;
    @PrimaryKey
    private int product_id;
    private String description;
    private int price;
    private int quantity;
    private String gender;
    private String image;
    private String ngaynhap;
    private int sale;
    private List<String> kichco;

    public Product() {
    }

    public Product(String name, int product_id, String description, int price, int quantity, String gender, String image, String ngaynhap, int sale, ArrayList<String> kichco) {
        this.name = name;
        this.product_id = product_id;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.gender = gender;
        this.image = image;
        this.ngaynhap = ngaynhap;
        this.sale = sale;
        this.kichco = kichco;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNgaynhap() {
        return ngaynhap;
    }

    public void setNgaynhap(String ngaynhap) {
        this.ngaynhap = ngaynhap;
    }

    public List<String> getKichco() {
        return kichco;
    }

    public void setKichco(List<String> kichco) {
        this.kichco = kichco;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }
    // ghi đè phương thức equals()  để xác định xem hai đối tượng Product có tương đương hay không dựa trên thuộc tính product_id hoặc thuộc tính khác.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Product other = (Product) obj;
        return product_id == other.product_id;
    }
}
