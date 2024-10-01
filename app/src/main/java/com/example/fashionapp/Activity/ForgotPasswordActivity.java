package com.example.fashionapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fashionapp.API.Utils;
import com.example.fashionapp.Model.ResponePost;
import com.example.fashionapp.R;
import com.example.fashionapp.Retrofit.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailEditText, otpEditText;
    Button sendOTPButton, submitButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.email);
        otpEditText = findViewById(R.id.otp);
        sendOTPButton = findViewById(R.id.btn_sendOTP);
        submitButton = findViewById(R.id.btn_submit);
        backButton = findViewById(R.id.btn_back);

        sendOTPButton.setOnClickListener(v -> {
            sendOTP();
        });
        submitButton.setOnClickListener(v -> {
            submitOTP();
        });
        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void sendOTP() {
        String email = emailEditText.getText().toString().trim();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.BASE_URL1)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ResponePost> call = apiInterface.sendOTP(email);

        call.enqueue(new Callback<ResponePost>() {
            @Override
            public void onResponse(Call<ResponePost> call, Response<ResponePost> response) {
                if (response.isSuccessful()) {
                    ResponePost responePost = response.body();
                    if (responePost != null && !responePost.isError()) {
                        Toast.makeText(getApplicationContext(), responePost.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), responePost != null ? responePost.getMessage() : "Error sending OTP", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to send OTP: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponePost> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to send OTP: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        otpEditText.setVisibility(View.VISIBLE); // Hiển thị EditText nhập OTP sau khi gửi OTP
    }

    private void submitOTP() {
        String otp = otpEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.BASE_URL1)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ResponePost> call = apiInterface.checkOTP(email, otp);

        call.enqueue(new Callback<ResponePost>() {
            @Override
            public void onResponse(Call<ResponePost> call, Response<ResponePost> response) {
                if (response.isSuccessful()) {
                    ResponePost responePost = response.body();
                    if (responePost != null && !responePost.isError()) {
                        Toast.makeText(getApplicationContext(), responePost.getMessage(), Toast.LENGTH_LONG).show();
                        // Thực hiện hành động khi OTP hợp lệ, ví dụ chuyển sang màn hình mới
                        Intent intent = new Intent(ForgotPasswordActivity.this, ForgotPassword2Activity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), responePost != null ? responePost.getMessage() : "Error verifying OTP", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to verify OTP: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponePost> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to verify OTP: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
