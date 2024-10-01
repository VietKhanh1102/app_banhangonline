package com.example.fashionapp.Retrofit;

import com.example.fashionapp.API.Utils;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    private static Retrofit instance;
    private static ApiInterface apiInterface;

    public static Retrofit getInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl(Utils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(
                            new GsonBuilder()
                                    .setLenient()
                                    .create()
                    ))
                    .build();
        }
        return instance;
    }

    public static ApiInterface getApi() {
        if (apiInterface == null) {
            apiInterface = getInstance().create(ApiInterface.class);
        }
        return apiInterface;
    }
    public static final String BASE_URL = "http://192.168.43.187/uniquilo/";

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS);

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())  // Add Gson converter
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }


}
