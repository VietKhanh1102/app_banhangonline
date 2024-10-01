package com.example.fashionapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ForgotPassword2Activity extends AppCompatActivity {

    EditText edtNewPass, edtConfirmPass;
    Button btnSignUp;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password2);

        edtNewPass = findViewById(R.id.edt_new_pass);
        edtConfirmPass = findViewById(R.id.edt_edt_confirm_pass);
        btnSignUp = findViewById(R.id.btn_sign_up);
        ivBack = findViewById(R.id.iv_back);


        String email = getIntent().getStringExtra("email");

        btnSignUp.setOnClickListener(v -> updatePassword(email));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updatePassword(String email) {
        String newPassword = edtNewPass.getText().toString().trim();
        String confirmNewPassword = edtConfirmPass.getText().toString().trim();

        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(getApplicationContext(), "Mật khẩu xác nhận không khớp!", Toast.LENGTH_LONG).show();
            return;
        }

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<ResponePost> call = apiInterface.forgotPassword(email, newPassword);

        call.enqueue(new Callback<ResponePost>() {
            @Override
            public void onResponse(Call<ResponePost> call, Response<ResponePost> response) {
                if (response.isSuccessful()) {
                    ResponePost responePost = response.body();
                    if (responePost != null && !responePost.isError()) {
                        Toast.makeText(getApplicationContext(), responePost.getMessage(), Toast.LENGTH_LONG).show();
                        // Quay lại LoginActivity và đóng ForgotPassword2Activity
                        Intent intent = new Intent(ForgotPassword2Activity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), responePost != null ? responePost.getMessage() : "Error updating password", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to update password: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponePost> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to update password: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
