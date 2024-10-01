package com.example.fashionapp.Model;

import java.io.Serializable;

public class Item implements Serializable {

   private String item;

    public Item(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}


