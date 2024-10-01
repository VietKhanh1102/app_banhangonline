package com.example.fashionapp.Model;

public class JsonResponse {
    private int http_code;
    private String data;

    public int getHttpCode() {
        return http_code;
    }

    public void setHttpCode(int http_code) {
        this.http_code = http_code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
