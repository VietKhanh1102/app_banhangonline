// ChiTietDonHangActivity.java
package com.example.fashionapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.fashionapp.Adapter.DonHangAdapter;
import com.example.fashionapp.Interface.APIService;
import com.example.fashionapp.Model.DonHangResponse;
import com.example.fashionapp.Model.Thanhtoan;
import com.example.fashionapp.Model.UserManager;
import com.example.fashionapp.R;
import com.example.fashionapp.Retrofit.ApiInterface;
import com.example.fashionapp.Retrofit.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietDonHangActivity extends AppCompatActivity {

    private List<Thanhtoan> listDonHang;
    private RecyclerView recyclerView;
    private DonHangAdapter donHangAdapter;
    private ApiInterface apiInterface;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);
        listDonHang = new ArrayList<>();
        AnhXa();
        callAPIListHistroy();
        onClickLister();
    }

    private void AnhXa() {
        apiInterface = RetrofitClient.getInstance().create(ApiInterface.class);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        imgBack = findViewById(R.id.iv_back);
    }

    private void onClickLister() {
        imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void callAPIListHistroy() {
        int userId = Integer.parseInt(UserManager.getInstance().getCurrentUser().getUser_id());
        APIService.apiService.xemDonHang(userId).enqueue(new Callback<DonHangResponse>() {
            @Override
            public void onResponse(Call<DonHangResponse> call, Response<DonHangResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DonHangResponse donHangResponse = response.body();
                    if (donHangResponse.isSuccess()) {
                        List<Thanhtoan> originalList = donHangResponse.getResult();
                        for (Thanhtoan thanhtoan : originalList) {
                            if (thanhtoan.getTrangthai() != 0) { // Loại bỏ đơn hàng ở trạng thái "đang xử lí"
                                listDonHang.add(thanhtoan);
                            }
                        }
                        donHangAdapter = new DonHangAdapter(listDonHang, ChiTietDonHangActivity.this, null); // Không cần truyền onClickListener
                        recyclerView.setAdapter(donHangAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<DonHangResponse> call, Throwable t) {
                // Xử lý khi gặp lỗi trong quá trình gọi API
            }
        });
    }
}
