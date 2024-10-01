package com.example.fashionapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.fashionapp.Model.ResponseUpDatePass;
import com.example.fashionapp.Model.User;
import com.example.fashionapp.Model.UserManager;
import com.example.fashionapp.R;
import com.example.fashionapp.Retrofit.ApiInterface;
import com.example.fashionapp.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    ApiInterface apiInterface;
    private ImageView ivBackPass;
    private EditText edtOldPass, edtNewPass, edtReNewPass;
    private Button btnUpdate;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        AnhXa();
        onClickListener();
    }

    private void AnhXa() {
        apiInterface = RetrofitClient.getApi();

        ivBackPass = findViewById(R.id.iv_back_pass);
        edtOldPass = findViewById(R.id.edt_old_pass);
        edtNewPass = findViewById(R.id.edt_new_pass);
        edtReNewPass = findViewById(R.id.edt_edt_confirm_pass);
        btnUpdate = findViewById(R.id.btn_update_pass);
    }

    private void onClickListener() {
        ivBackPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String id = UserManager.getInstance().getCurrentUser().getUser_id();
        String oldPassword = edtOldPass.getText().toString();
        String newPassword = edtNewPass.getText().toString();
        String reNewPassword = edtReNewPass.getText().toString();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || reNewPassword.isEmpty()) {
            Toast.makeText(ChangePasswordActivity.this, "Vui lòng nhập đầy đủ thông tin mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(reNewPassword)) {
            Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới và mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        apiInterface.updatePass(id, oldPassword, newPassword)
                .enqueue(new Callback<ResponseUpDatePass>() {
                    @Override
                    public void onResponse(Call<ResponseUpDatePass> call, Response<ResponseUpDatePass> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                String status = response.body().getMessage();
                                Toast.makeText(ChangePasswordActivity.this, status, Toast.LENGTH_LONG).show();

                                // Nếu cập nhật thành công, đăng xuất người dùng và chuyển về màn hình đăng nhập
                                if (status.equals("Thay đổi thành công, mời bạn đăng nhập lại!")) {
                                    UserManager.getInstance().setLoggedIn(false);
                                    Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseUpDatePass> call, Throwable t) {
                        Toast.makeText(ChangePasswordActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
    }


}
