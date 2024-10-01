package com.example.fashionapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.fashionapp.Model.User;
import com.example.fashionapp.Model.UserManager;
import com.example.fashionapp.R;
import com.example.fashionapp.Retrofit.ApiInterface;
import com.example.fashionapp.Retrofit.LoginResponse;
import com.example.fashionapp.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmailSignIn, edtPasswordSignIn;
    private Button btnSignIn;
    private ApiInterface apiInterface;
    private TextView txtSignUp, tvPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AnhXa();
        initControll();

        // Khởi tạo đối tượng ApiInterface từ RetrofitClient
        apiInterface = RetrofitClient.getApi();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmailSignIn.getText().toString().trim();
                String password = edtPasswordSignIn.getText().toString().trim();

                // Gửi thông tin đăng nhập đến backend để xác thực
                Call<LoginResponse> call = apiInterface.login(email, password);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.getStatus() == 1) { // Đã đăng nhập thành công
                                User user = loginResponse.getUser();
                                UserManager.getInstance().setCurrentUser(user);
                                UserManager.getInstance().setLoggedIn(true);
                                // Chuyển sang màn hình chính
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else { // Đăng nhập thất bại
                                Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else { // Đăng nhập thất bại do lỗi server
                            Toast.makeText(LoginActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        // Xử lý khi gặp lỗi kết nối
                        Toast.makeText(LoginActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    private void initControll() {
        // Xử lý sự kiện khi người dùng chọn đăng ký
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện khi người dùng chọn quên mật khẩu
        tvPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void AnhXa() {
        edtEmailSignIn=findViewById( R.id.edt_email_in );
        edtPasswordSignIn=findViewById( R.id.edt_password_in );
        btnSignIn=findViewById( R.id.btn_sign_in );
        txtSignUp=findViewById(R.id.txt_sign_up);
        tvPassword = findViewById(R.id.tvForgotPassword);
    }
}
