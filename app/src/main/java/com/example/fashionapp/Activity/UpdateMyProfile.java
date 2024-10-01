package com.example.fashionapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fashionapp.Model.ResponseUpDateUser;
import com.example.fashionapp.Model.User;
import com.example.fashionapp.Model.UserManager;
import com.example.fashionapp.R;
import com.example.fashionapp.Retrofit.ApiInterface;
import com.example.fashionapp.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateMyProfile extends AppCompatActivity {
    ApiInterface apiInterface;
    private ImageView ivBackAddress;
    private TextView tvEmail;
    private EditText edtChangeName, edtChangeCity, edtChangeAddress, edtChangePhone;
    private Button btnUpdate;
    private RadioButton radio_Nam, radio_nu;
    private User user;

    // Biến để kiểm tra trạng thái của các trường thông tin
    private boolean isNameFilled = true;
    private boolean isPhoneFilled = true;
    private boolean isAddressFilled = true;
    private boolean isCityFilled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_profile);
        AnhXa();
        onClickListener();
    }

    private void onClickListener() {
        ivBackAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
    }

    private void updateUser() {
        String id = UserManager.getInstance().getCurrentUser().getUser_id();
        String email = tvEmail.getText().toString().trim();
        String password = user.getPassword();

        // Kiểm tra các trường thông tin trước khi cập nhật
        if (isNameFilled && isPhoneFilled && isAddressFilled && isCityFilled) {
            apiInterface.updateUser(id, user.getUsername(), user.getGender(), user.getPhoneNumber(), user.getAddress(), user.getCity(), email, password)
                    .enqueue(new Callback<ResponseUpDateUser>() {
                        @Override
                        public void onResponse(Call<ResponseUpDateUser> call, Response<ResponseUpDateUser> response) {
                            if (response.isSuccessful()) {

                                if (response.body() != null) {
                                    String status = String.valueOf(response.body().getMessage());
                                    Log.d("AAA", "aa" + status);
                                }
                                UserManager.getInstance().setCurrentUser(user);
                                Toast.makeText(UpdateMyProfile.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                // Sau khi cập nhật thông tin người dùng thành công
                                Intent returnIntent = new Intent();
                                setResult(RESULT_OK, returnIntent);
                                finish();
                            } else {
                                Toast.makeText(UpdateMyProfile.this, "Cập nhật thất ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseUpDateUser> call, Throwable t) {
                            Toast.makeText(UpdateMyProfile.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });
        } else {
            // Hiển thị thông báo nếu có trường thông tin nào đó chưa được điền
            Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }
    }

    private void AnhXa() {
        apiInterface = RetrofitClient.getApi();

        ivBackAddress = findViewById(R.id.iv_back_address);
        tvEmail = findViewById(R.id.tv_email);
        edtChangeName = findViewById(R.id.edt_change_name);
        edtChangeCity = findViewById(R.id.edt_change_city);
        edtChangeAddress = findViewById(R.id.edt_change_address);
        edtChangePhone = findViewById(R.id.edt_change_phone);
        radio_Nam = findViewById(R.id.radio_nam);
        radio_nu = findViewById(R.id.radio_nu);
        btnUpdate = findViewById(R.id.btn_update);

        user = UserManager.getInstance().getCurrentUser();

        tvEmail.setText(user.getEmail());
        edtChangeName.setText(user.getUsername());
        edtChangeAddress.setText(user.getAddress());
        edtChangeCity.setText(user.getCity());
        edtChangePhone.setText(user.getPhoneNumber());

        if (user.getGender().equals("Nam")) {
            radio_Nam.setChecked(true);
        } else {
            radio_nu.setChecked(true);
        }

        // Listener cho các trường thông tin
        edtChangeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(edtChangeName.getText().toString().trim())) {
                    isNameFilled = false;
                } else {
                    isNameFilled = true;
                    user.setUsername(edtChangeName.getText().toString().trim());
                }
            }
        });

        edtChangePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(edtChangePhone.getText().toString().trim())) {
                    isPhoneFilled = false;
                } else {
                    isPhoneFilled = true;
                    user.setPhoneNumber(edtChangePhone.getText().toString().trim());
                }
            }
        });

        edtChangeAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(edtChangeAddress.getText().toString().trim())) {
                    isAddressFilled = false;
                } else {
                    isAddressFilled = true;
                    user.setAddress(edtChangeAddress.getText().toString().trim());
                }
            }
        });

        edtChangeCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(edtChangeCity.getText().toString().trim())) {
                    isCityFilled = false;
                } else {
                    isCityFilled = true;
                    user.setCity(edtChangeCity.getText().toString().trim());
                }
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        // Thực hiện các thao tác khác (nếu cần) khi quay lại màn hình trước đó.
    }
}
