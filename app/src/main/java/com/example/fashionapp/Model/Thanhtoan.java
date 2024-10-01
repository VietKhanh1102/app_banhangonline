package com.example.fashionapp.Model;

import java.io.Serializable;
import java.util.List;

public class Thanhtoan implements Serializable {
    int order_id;
    int user_id;
    String diachi;
    String sdt;
    int total_money;
    int payment_methods;
    List<ItemDonHang> item;
    int trangthai;

    public Thanhtoan(int order_id, int user_id, String diachi, String sdt, int total_money, int payment_methods, List<ItemDonHang> item) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.diachi = diachi;
        this.sdt = sdt;
        this.total_money = total_money;
        this.payment_methods = payment_methods;
        this.item = item;
        this.trangthai = trangthai;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public int getTotal_money() {
        return total_money;
    }

    public void setTotal_money(int total_money) {
        this.total_money = total_money;
    }

    public int getPayment_methods() {
        return payment_methods;
    }

    public void setPayment_methods(int payment_methods) {
        this.payment_methods = payment_methods;
    }

    public List<ItemDonHang> getItem() {
        return item;
    }

    public void setItem(List<ItemDonHang> item) {
        this.item = item;
    }
}
