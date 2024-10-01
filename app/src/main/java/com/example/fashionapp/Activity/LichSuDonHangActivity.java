package com.example.fashionapp.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fashionapp.Adapter.DonHangAdapter;
import com.example.fashionapp.Interface.APIService;
import com.example.fashionapp.Model.DonHangResponse;
import com.example.fashionapp.Model.ItemDonHang;
import com.example.fashionapp.Model.ResponePost;
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

public class LichSuDonHangActivity extends AppCompatActivity {
    private List<Thanhtoan> reversedList;
    private RecyclerView recyclerView;
    private DonHangAdapter donHangAdapter;
    private ImageView imgBack;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_don_hang);
        reversedList = new ArrayList<>();
        AnhXa();
        onClickListener();
        callAPIListHistory();
    }

    private void AnhXa() {
        apiInterface = RetrofitClient.getInstance().create(ApiInterface.class);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        imgBack = findViewById(R.id.iv_back);
    }

    private void showDialogView(Thanhtoan thanhtoan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn xóa đơn hàng này?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        callAPIDelete(thanhtoan);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void callAPIListHistory() {
        int userId = Integer.parseInt(UserManager.getInstance().getCurrentUser().getUser_id());
        APIService.apiService.xemDonHang(userId).enqueue(new Callback<DonHangResponse>() {
            @Override
            public void onResponse(Call<DonHangResponse> call, Response<DonHangResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DonHangResponse donHangResponse = response.body();
                    if (donHangResponse.isSuccess()) {
                        List<Thanhtoan> originalList = donHangResponse.getResult();
                        for (int i = originalList.size() - 1; i >= 0; i--) {
                            Thanhtoan item = originalList.get(i);
                            if (item.getTrangthai() == 0) { // Chỉ thêm đơn hàng đang xử lí vào danh sách
                                reversedList.add(item);
                            }
                        }
                        donHangAdapter = new DonHangAdapter(reversedList, LichSuDonHangActivity.this, thanhtoan -> showDialogView(thanhtoan));
                        recyclerView.setAdapter(donHangAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<DonHangResponse> call, Throwable t) {
                // Handle API call failure
                Log.e("LichSuDonHangActivity", "Failed to fetch order history: " + t.getMessage());
            }
        });
    }

    private void callAPIDelete(Thanhtoan thanhtoan) {
        apiInterface.deleteBill(thanhtoan.getOrder_id()).enqueue(new Callback<ResponePost>() {
            @Override
            public void onResponse(Call<ResponePost> call, Response<ResponePost> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(LichSuDonHangActivity.this, "Bạn đã xóa đơn hàng", Toast.LENGTH_SHORT).show();
                    updateProductQuantityOnDelete(thanhtoan); // Cập nhật lại số lượng sản phẩm
                    reversedList.remove(thanhtoan);
                    donHangAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(LichSuDonHangActivity.this, "Không thể xóa đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponePost> call, Throwable t) {
                Toast.makeText(LichSuDonHangActivity.this, "Không thể xóa đơn hàng", Toast.LENGTH_SHORT).show();
                Log.e("LichSuDonHangActivity", "Failed to delete order: " + t.getMessage());
            }
        });
    }

    private void updateProductQuantityOnDelete(Thanhtoan thanhtoan) {
        List<ItemDonHang> itemList = thanhtoan.getItem();

        for (ItemDonHang item : itemList) {
            int productId = item.getIdsp();
            int quantity = item.getSoluong();

            // Gọi API để cập nhật lại số lượng sản phẩm
            apiInterface.updateProductQuantityOnDelete(productId, quantity)
                    .enqueue(new Callback<ResponePost>() {
                        @Override
                        public void onResponse(Call<ResponePost> call, Response<ResponePost> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
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

    private void onClickListener() {
        imgBack.setOnClickListener(v -> onBackPressed());
    }
}
