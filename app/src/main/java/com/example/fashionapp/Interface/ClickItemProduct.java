package com.example.fashionapp.Interface;

import com.example.fashionapp.Model.Product;

public interface ClickItemProduct {
    void onItemProductClick(Product product);
    void onClickFavoriteItem(int pos);
}
