package com.example.fashionapp.Model;

public class ItemDonHang {
    private int idsp;
    private int soluong;
    private int giasp;
    private String name;
    private String image;
    private String gender;

    public ItemDonHang(int idsp, int soluong, int giasp, String name, String image, String gender) {
        this.idsp = idsp;
        this.soluong = soluong;
        this.giasp = giasp;
        this.name = name;
        this.image = image;
        this.gender = gender;
    }

    public int getIdsp() {
        return idsp;
    }

    public void setIdsp(int idsp) {
        this.idsp = idsp;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public int getGiasp() {
        return giasp;
    }

    public void setGiasp(int giasp) {
        this.giasp = giasp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
