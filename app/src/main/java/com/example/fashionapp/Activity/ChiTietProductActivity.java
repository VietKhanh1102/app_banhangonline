package com.example.fashionapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fashionapp.API.Utils;
import com.example.fashionapp.Model.GioHang;
import com.example.fashionapp.Model.Product;
import com.example.fashionapp.R;
import com.example.fashionapp.dataLocal.Database;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietProductActivity extends AppCompatActivity {
    private boolean isFavorite = false;
    private ImageButton btnFavorite;
    private Button btnThem;
    private TextView tvTenSP, tvGia, tvMaSP, tvMoTa, tvGiaSale, tvPhanTramSale, tvSaleOff, soluongsp;
    private ImageView imgSP, imgBack, imgGioHang;
    private Spinner spnKichCo;
    private NotificationBadge badge;
    private Product productList;
    private boolean isAnimationRunning = false;
    private LinearLayout linearLayout;
    private ImageView imgMota;
    private Handler handler = new Handler();
    private EditText edtSoLuong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_product);
        // Lấy dữ liệu từ Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        productList = (Product) bundle.getSerializable("object_product");

        // Ánh xạ các view
        initView();

        // Hiển thị thông tin sản phẩm
        displayProductInfo();

        // Xử lý sự kiện khi click các button và view khác
        onClickListener();
    }

    private void initView() {
        btnFavorite = findViewById(R.id.btn_them_favorite);
        btnThem = findViewById(R.id.btn_them_gio_hang);
        tvTenSP = findViewById(R.id.tv_chitiet_product);
        tvGia = findViewById(R.id.tv_gia);
        tvMaSP = findViewById(R.id.tv_ma_product);
        tvMoTa = findViewById(R.id.tv_mo_ta);
        imgSP = findViewById(R.id.img_product);
        imgBack = findViewById(R.id.iv_back);
        imgGioHang = findViewById(R.id.gio_hang_a);
        spnKichCo = findViewById(R.id.spn_kich_co);
        badge = findViewById(R.id.menu_sl);
        tvGiaSale = findViewById(R.id.tv_cost_sale);
        tvSaleOff = findViewById(R.id.tv_sale_off);
        tvPhanTramSale = findViewById(R.id.tvphantramSale);
        linearLayout = findViewById(R.id.linea_mota);
        imgMota = findViewById(R.id.img_1);
        edtSoLuong = findViewById(R.id.edt_so_luong); // Thay thế spnSoLuong bằng edtSoLuong
        soluongsp = findViewById(R.id.soluongsp);

        // ArrayAdapter cho Spinner Kích cỡ
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, productList.getKichco());
        spnKichCo.setAdapter(adapter1);

        // Cập nhật badge số lượng trong giỏ hàng
        updateBadge();
    }

    private void displayProductInfo() {
        Glide.with(getApplicationContext()).load(productList.getImage()).into(imgSP);
        tvTenSP.setText(productList.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        int giaSale = productList.getPrice() - (productList.getPrice() * productList.getSale() / 100);

        if (productList.getSale() == 0) {
            tvGiaSale.setVisibility(View.GONE);
            tvPhanTramSale.setVisibility(View.GONE);
            tvSaleOff.setVisibility(View.GONE);
            // Xóa gạch ngang khi không có giá sale
            tvGia.setPaintFlags(tvGia.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            tvGia.setTextColor(Color.BLACK);
        } else {
            tvGiaSale.setVisibility(View.VISIBLE);
            tvGiaSale.setText(decimalFormat.format(giaSale) + " VND");
            tvSaleOff.setVisibility(View.VISIBLE);
            tvPhanTramSale.setVisibility(View.VISIBLE);
            tvPhanTramSale.setText("-" + decimalFormat.format(productList.getSale()) + "%");
            // Gạch ngang tvgiaProduct
            tvGia.setPaintFlags(tvGia.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvGia.setTextColor(Color.GRAY);
        }

        tvGia.setText(decimalFormat.format(productList.getPrice()) + " VND");
        tvMaSP.setText(String.valueOf(productList.getProduct_id()));
        tvMoTa.setText(productList.getDescription());

        soluongsp.setText(String.valueOf(productList.getQuantity()));

    }

    private void onClickListener() {
        // Xử lý sự kiện click nút quay lại
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Xử lý sự kiện click vào giỏ hàng
        imgGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietProductActivity.this, GioHangActivity.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện click thêm vào yêu thích
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnimationRunning) {
                    return;
                }

                isAnimationRunning = true;
                animateFavoriteButton();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isFavorite) {
                            btnFavorite.setImageResource(R.drawable.baseline_favorite_24);
                            // Xử lý gỡ khỏi yêu thích
                            deleteFavorite();
                            Toast.makeText(ChiTietProductActivity.this, "Đã gỡ khỏi yêu thích", Toast.LENGTH_SHORT).show();
                        } else {
                            btnFavorite.setImageResource(R.drawable.ic_read_favorite);
                            // Xử lý thêm vào yêu thích
                            addFavorite();
                            Toast.makeText(ChiTietProductActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                        }

                        isFavorite = !isFavorite;
                        isAnimationRunning = false;
                    }
                }, 100);
            }
        });

        // Xử lý sự kiện click thêm vào giỏ hàng
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themGioHang();
                //Toast.makeText(ChiTietProductActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện click mở rộng/ thu gọn mô tả sản phẩm
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvMoTa.getVisibility() == View.GONE) {
                    tvMoTa.setVisibility(View.VISIBLE);
                    imgMota.setImageResource(R.drawable.ic_baseline_expand_more);
                } else {
                    tvMoTa.setVisibility(View.GONE);
                    imgMota.setImageResource(R.drawable.ic_baseline_chevron_left);
                }
            }
        });

        // Xử lý nhập số lượng từ EditText
        edtSoLuong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý trước khi thay đổi
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Không cần xử lý khi đang thay đổi
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Xử lý sau khi đã thay đổi nội dung của EditText
                updateBadge(); // Cập nhật badge khi số lượng thay đổi
            }
        });
    }
    private void deleteFavorite() {
        Database.getInstance(this).favouriteDAO().deleteFavourite(productList);
    }

    private void addFavorite() {
        Database.getInstance(this).favouriteDAO().insertFavourite(productList);
    }

    private void animateFavoriteButton() {
        Animation anim = new ScaleAnimation(1f, 0.9f, 1f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(100);
        anim.setFillAfter(true);
        btnFavorite.startAnimation(anim); // Thêm phương thức startAnimation để bắt đầu animation
    }


    private void themGioHang() {
        // Lấy số lượng từ EditText
        String soLuongStr = edtSoLuong.getText().toString();
        if (soLuongStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
            return;
        }

        int soluong = Integer.parseInt(soLuongStr);
        String kichco = spnKichCo.getSelectedItem().toString();
        boolean flag = false;

        // Kiểm tra số lượng mua có vượt quá số lượng còn lại không
        if (soluong > productList.getQuantity()) {
            Toast.makeText(this, "Số lượng mua hàng vượt quá số lượng còn lại", Toast.LENGTH_SHORT).show();
            return; // Không thêm vào giỏ hàng nếu số lượng mua vượt quá số lượng còn lại
        }

        // Kiểm tra số lượng mua có lớn hơn 0 không
        if (soluong <= 0) {
            Toast.makeText(this, "Số lượng mua hàng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return; // Không thêm vào giỏ hàng nếu số lượng mua không hợp lệ
        }

        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            GioHang gioHang = Utils.manggiohang.get(i);

            if (gioHang.getProduct_id() == productList.getProduct_id() && gioHang.getKichco().equals(kichco)) {
                gioHang.setSoluong(soluong + gioHang.getSoluong());
                gioHang.setPrice(productList.getPrice());
                flag = true;
                break;
            }
        }

        if (!flag) {
            GioHang gioHang = new GioHang();
            int giaSale = productList.getPrice() - (productList.getPrice() * productList.getSale() / 100);
            gioHang.setPrice(giaSale);
            gioHang.setSoluong(soluong);
            gioHang.setProduct_id(productList.getProduct_id());
            gioHang.setImage(productList.getImage());
            gioHang.setKichco(kichco);
            gioHang.setName(productList.getName());
            gioHang.setGender(productList.getGender());

            Utils.manggiohang.add(gioHang);
        }

        // Cập nhật lại số lượng trên badge
        updateBadge();

        // Hiển thị thông báo đã thêm vào giỏ hàng
        Toast.makeText(this, "Đã thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
    }



    private void updateBadge() {
        int totalItem = 0;
        for (GioHang gioHang : Utils.manggiohang) {
            totalItem += gioHang.getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

}
