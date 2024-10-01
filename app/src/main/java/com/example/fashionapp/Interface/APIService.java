package com.example.fashionapp.Interface;

import com.example.fashionapp.API.Utils;
import com.example.fashionapp.Model.DataProduct;
import com.example.fashionapp.Model.DonHangResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    //http://localhost/uniquilo/getsanpham.php

    //http://localhost/uniquilo/getaonu/readAoNu.php

    Gson gson = new GsonBuilder()
            .setLenient()
            .setDateFormat("dd-MM-yyy").create();
    //https://jsonplaceholder.typicode.com/posts?userId=1
    //http://192.168.1.6/uniquilo/customers/read.php
    //
    APIService apiService = new Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);

    APIService apiServiceNu = new Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);

    // http://localhost/uniquilo/getkids/readao.php

    APIService apiServiceKids = new Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);

    APIService apiServiceSearch = new Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);

    @GET("getMan/read.php")
    Call<DataProduct> getLisAoMen();
    @GET("getMan/readDoMacNgoai.php")
    Call<DataProduct> getLisDoMacNgoaiMen();

    @GET("getMan/readQuan.php")
    Call<DataProduct> getLisQuanMen();

    @GET("getMan/readDoLot.php")
    Call<DataProduct> getLisDoLot();

    @GET("getMan/readDoMacNha.php")
    Call<DataProduct> getLisDoMacNha();

    @GET("getWommen/readAoNu.php")
    Call<DataProduct> getLisAoWomen();

    @GET("getWommen/readDoMacNgoai.php")
    Call<DataProduct> getLisDoMacNgoaiWomem();

    @GET("getWommen/readQuan.php")
    Call<DataProduct> getLisQuanWomen();

    @GET("getWommen/readVay.php")
    Call<DataProduct> getLisVayWomen();

    @GET("getWommen/readBau.php")
    Call<DataProduct> getLisBauWomen();

    @GET("getWommen/readDoLot.php")
    Call<DataProduct> getLisLotWomen();

    @GET("getkids/readao.php")
    Call<DataProduct> getLisAoKids();

    @GET("sanphamnoibat/read.php")
    Call<DataProduct> getSanPhamNoiBat();

    @GET("sanphamnoibat/readSale.php")
    Call<DataProduct> getSanPhamSale();

    @GET("sanphamnoibat/readSanPhamMoi.php")
    Call<DataProduct> getSanPhamMoiNam();


    @GET("sanphamnoibat/readSanPhamNu.php")
    Call<DataProduct> getSanPhamMoiNu();

    @GET("postTimKiem/getSearch.php")
    Call<DataProduct> search(
            @Query("search") String search
    );

    @POST("xemdonhang.php")
    @FormUrlEncoded
    Call<DonHangResponse> xemDonHang(
            @Field("user_id") int user_id
            );
}


