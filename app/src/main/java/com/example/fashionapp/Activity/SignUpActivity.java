package com.example.fashionapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.fashionapp.API.Utils;
import com.example.fashionapp.R;
import com.example.fashionapp.Retrofit.ApiInterface;
import com.example.fashionapp.Retrofit.RetrofitClient;
import com.example.fashionapp.Retrofit.SignUpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    private EditText editUsername, editPhoneNumber, editAddress, editCity, editEmail, editPassword, editRePassword;
    private RadioButton rbNam, rbNu;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        AnhXa();
        initControll();
    }


    private void initControll() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangKy();
            }
        });

    }

    private void dangKy() {
        String str_user_name = editUsername.getText().toString().trim();
        String str_phone_number = editPhoneNumber.getText().toString().trim();
        String str_address = editAddress.getText().toString().trim();
        String str_city = editCity.getText().toString().trim();
        String str_email = editEmail.getText().toString().trim();
        String str_password = editPassword.getText().toString().trim();
        String str_repassword = editRePassword.getText().toString().trim();
        //checked nam nu
        Boolean isGenderChecked = rbNam.isChecked() || rbNu.isChecked();
        boolean isMale = isGenderChecked && rbNam.isChecked();

        // Kiểm tra mật khẩu theo mẫu
//        String passwordPattern = "^(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
//        if (!str_password.matches(passwordPattern)) {
//            editPassword.setError("Mật khẩu phải chứa ít nhất 8 ký tự, 1 chữ viết hoa, và 1 ký tự đặc biệt");
//            editPassword.requestFocus();
//            return;
//        }

        // Kiểm tra địa chỉ email theo mẫu
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern = "[a-zA-Z0-9._-]+@gmail\\.com";
        if (!str_email.matches(emailPattern)) {
            editEmail.setError("Địa chỉ email không hợp lệ");
            editEmail.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(str_user_name)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập tên người dùng", Toast.LENGTH_SHORT).show();
        } else if (!isGenderChecked) {
            Toast.makeText(getApplicationContext(), "Bạn chưa chọn giới tính", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_phone_number)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập số điên thoại", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_address)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(str_city)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập thành phố", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_email)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_password)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
        } else {
            if (str_password.equals(str_repassword)) {
                apiInterface.dangky(str_user_name, isMale ? "Nam" : "Nữ", str_phone_number, str_address,str_city, str_email, str_password).enqueue(new Callback<SignUpResponse>() {
                    @Override
                    public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Đăng ký thất bại" + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SignUpResponse> call, Throwable t) {
                        Toast.makeText(SignUpActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Nhập lại mật khẩu sai", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void AnhXa() {
        apiInterface = RetrofitClient.getApi();

        editUsername = findViewById(R.id.edt_username);
        rbNam = findViewById(R.id.rb_nam);
        rbNu = findViewById(R.id.rb_nu);
        editPhoneNumber = findViewById(R.id.edt_phone_number);
        editAddress = findViewById(R.id.edt_address);
        editCity = findViewById(R.id.edt_city);
        editEmail = findViewById(R.id.edt_email);
        editPassword = findViewById(R.id.edt_password);
        editRePassword = findViewById(R.id.edt_repassword);
        btnSignUp = findViewById(R.id.btn_sign_up);
    }

}