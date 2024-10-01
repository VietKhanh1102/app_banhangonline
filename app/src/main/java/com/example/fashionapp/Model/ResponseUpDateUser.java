package com.example.fashionapp.Model;

import com.google.gson.annotations.SerializedName;

public class ResponseUpDateUser {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseUpDateUser(String message) {
        this.message = message;
    }

    @SerializedName("message")
    private String message;
}
