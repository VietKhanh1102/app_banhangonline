package com.example.fashionapp;

import androidx.lifecycle.ViewModel;

import com.example.fashionapp.Model.User;
import com.example.fashionapp.Model.UserManager;

import java.util.ArrayList;
import java.util.List;

;

public class MainViewModel extends ViewModel {
    public List<User> users = new ArrayList<>();

    public User user = UserManager.getInstance().getCurrentUser();
}
