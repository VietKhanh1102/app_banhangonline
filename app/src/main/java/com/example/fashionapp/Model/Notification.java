package com.example.fashionapp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Notification {
    private int id;
    private String message;
    @SerializedName("created_at")
    private Date createdAt;

    public Notification(int id, String message, Date createdAt) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
