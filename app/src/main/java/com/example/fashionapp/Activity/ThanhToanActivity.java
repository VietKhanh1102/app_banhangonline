package com.example.fashionapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fashionapp.API.Utils;
import com.example.fashionapp.Adapter.ThanhToanAdapter;
import com.example.fashionapp.Model.ResponePost;
import com.example.fashionapp.Model.UserManager;
import com.example.fashionapp.R;
import com.example.fashionapp.Retrofit.ApiInterface;
import com.example.fashionapp.Retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThanhToanActivity extends AppCompatActivity {
    private ImageView btnBack;
    private Button btnDatHang;
    private RecyclerView recyclerView;
    private ThanhToanAdapter adapter;
    CompositeDisposable compositeDisposable= new CompositeDisposable();
    private ApiInterface apiInterface;
    private TextView tvEdit, tvNameUser, tvSDT, tvAdress, tvTongCong, tvTong, tvTongTien, tvNgayThang;
    private RadioGroup radioGroup;
    private RadioButton button1, button2;
    private EditText edtNote;
    private String note = "";
    private String selectedOption="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        AnhXa();
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ThanhToanAdapter(this, Utils.manggiohang);
        recyclerView.setAdapter(adapter);
        tinhTongTien();

        // Lấy ngày tháng hiện tại
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        // Cộng thêm 5 ngày
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        String futureDate = dateFormat.format(calendar.getTime());

        // Nối ngày tháng hiện tại với ngày tháng sau 5 ngày
        String result = currentDate + " - " + futureDate;

        // Hiển thị kết quả lên TextView
        tvNgayThang.setText(result);
        onClickListen();
    }

    private void AnhXa() {
        apiInterface = RetrofitClient.getInstance().create(ApiInterface.class);
        btnBack = findViewById(R.id.iv_back);
        recyclerView = findViewById(R.id.recycler);
        tvNameUser = findViewById(R.id.tv_name_user);
        tvSDT = findViewById(R.id.tv_sdt);
        tvAdress = findViewById(R.id.tv_dia_chi);
        tvTongCong = findViewById(R.id.tv_tong_cong);
//        tvTong = findViewById(R.id.tv_tong);
        tvTongTien = findViewById(R.id.tv_tongdon);
        tvNgayThang = findViewById(R.id.tv_date_data);
        btnDatHang = findViewById(R.id.btn_dathang);
        radioGroup = findViewById(R.id.radio_group);
        button1 = findViewById(R.id.radio_nhan_hang);
        button2 = findViewById(R.id.radio_zalopay);
//        edtNote = findViewById(R.id.edt_note);
        tvEdit = findViewById(R.id.tv_edit);
        if (button1.isChecked()) {
            selectedOption = "1";
        } else {
            selectedOption = "2";
        }
    }

    private void tinhTongTien() {
        int sum = 0;
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            sum += Utils.manggiohang.get(i).getPrice() * Utils.manggiohang.get(i).getSoluong();
        }
        // Loại bỏ phí vận chuyển
        int total = sum;
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvTongCong.setText(decimalFormat.format(sum) + " VND");
        //tvTong.setText(decimalFormat.format(total) + " VND");
        tvTongTien.setText(decimalFormat.format(total) + " VND");
    }


    private void onClickListen() {
        tvNameUser.setText(UserManager.getInstance().getCurrentUser().getUsername());
        tvSDT.setText(UserManager.getInstance().getCurrentUser().getPhoneNumber());
        tvAdress.setText(UserManager.getInstance().getCurrentUser().getAddress() + ", " +
                UserManager.getInstance().getCurrentUser().getCity());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (button1.isChecked()) {
                    selectedOption = "1";
                } else {
                    selectedOption = "2";
                }
            }
        });


        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        btnDatHang.setOnClickListener(v -> {
            // Lấy ngày tháng hiện tại
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String currentDate = dateFormat.format(calendar.getTime());

            int id = Integer.parseInt(UserManager.getInstance().getCurrentUser().getUser_id());
            String sdt = UserManager.getInstance().getCurrentUser().getPhoneNumber();
            String diachi = UserManager.getInstance().getCurrentUser().getAddress() + ", " + UserManager.getInstance().getCurrentUser().getCity();
            String email = UserManager.getInstance().getCurrentUser().getEmail();

            int tongtien = 0;
            int totalItem = 0;
            String status = "1";

            for (int i = 0; i < Utils.manggiohang.size(); i++) {
                tongtien = tongtien + Utils.manggiohang.get(i).getPrice() * Utils.manggiohang.get(i).getSoluong();
                totalItem += Utils.manggiohang.get(i).getSoluong();
            }

            String chitiet = new Gson().toJson(Utils.manggiohang);

            apiInterface.createOder(id, totalItem, tongtien, status, selectedOption, note, email, sdt, diachi, currentDate, chitiet)
                    .enqueue(new Callback<ResponePost>() {
                        @Override
                        public void onResponse(Call<ResponePost> call, Response<ResponePost> response) {
                            if (response.body() != null && response.body().isSuccess()) {
                                Toast.makeText(ThanhToanActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                updateProductQuantity(); // Gọi hàm cập nhật số lượng sản phẩm
                                Utils.manggiohang.clear();
                                Intent intent = new Intent(ThanhToanActivity.this, MainActivity.class);
                                finishAffinity();
                                startActivity(intent);
                            } else {
                                Toast.makeText(ThanhToanActivity.this, "Lỗi đặt hàng", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponePost> call, Throwable t) {
                            Log.d("TAGa", "onFailure: " + t.getMessage());
                            Toast.makeText(ThanhToanActivity.this, "Fail: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThanhToanActivity.this, UpdateMyProfile.class);
                startActivityForResult(intent, 1); // Sử dụng request code 1
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Cập nhật thông tin hiển thị trên màn hình
            tvNameUser.setText(UserManager.getInstance().getCurrentUser().getUsername());
            tvSDT.setText(UserManager.getInstance().getCurrentUser().getPhoneNumber());
            tvAdress.setText(UserManager.getInstance().getCurrentUser().getAddress() + ", " +
                    UserManager.getInstance().getCurrentUser().getCity());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        compositeDisposable.clear();
        super.onPause();
    }

    private void updateProductQuantity() {
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            int productId = Utils.manggiohang.get(i).getProduct_id();
            int quantity = Utils.manggiohang.get(i).getSoluong();

            // Gọi API để cập nhật quantity cho sản phẩm
            apiInterface.updateProductQuantity(productId, quantity)
                    .enqueue(new Callback<ResponePost>() {
                        @Override
                        public void onResponse(Call<ResponePost> call, Response<ResponePost> response) {
                            if (response.body() != null && response.body().isSuccess()) {
                                Log.d("updateProductQuantity", "Cập nhật số lượng thành công cho sản phẩm: " + productId);
                            } else {
                                Log.e("updateProductQuantity", "Cập nhật số lượng thất bại cho sản phẩm: " + productId);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponePost> call, Throwable t) {
                            Log.e("updateProductQuantity", "Cập nhật số lượng thất bại: " + t.getMessage());
                        }
                    });
        }
    }
}
