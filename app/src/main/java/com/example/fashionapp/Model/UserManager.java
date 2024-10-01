package com.example.fashionapp.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


public class UserManager {
    private static UserManager instance;
    private boolean isLoggedIn;
    private User currentUser;
    SharedPreferences sharedPreferences;

    private UserManager() {
        isLoggedIn = false;
        currentUser = null;
    }

    // khởi tạo SharePreferences
    public void init(Context context) {
         sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
//         isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
         String userJson = sharedPreferences.getString("user", null);
         isLoggedIn = userJson != null;
         if (userJson != null) {
             //  chuyển json sang User
             User user = new Gson().fromJson(userJson, User.class);
             currentUser = user;
         }
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        sharedPreferences.edit()
                .putBoolean("isLoggedIn", true)
                .apply();
        isLoggedIn = loggedIn;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
        String json = new Gson().toJson(user);
        sharedPreferences.edit()
                .putString("user", json)
                .apply();
    }

    public void dangXuat() {
        sharedPreferences.edit()
                .putString("user", null)
                .apply();
    }
}
