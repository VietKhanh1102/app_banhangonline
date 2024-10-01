package com.example.fashionapp.Model;

public class Message {
    public static String SENT_BY_ME = "user";
    public static String SENT_BY_BOT = "bot";

    private String message;
    private String sent_by;
    private String user_id;
    private String id;

    public Message(String message, String sentBy, String user_id) {
        this.message = message;
        this.sent_by = sentBy;
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentBy() {
        return sent_by;
    }

    public void setSentBy(String sentBy) {
        this.sent_by = sentBy;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
