package com.example.fashionapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fashionapp.API.Utils;
import com.example.fashionapp.Adapter.ListProductAdapter;
import com.example.fashionapp.Interface.APIService;
import com.example.fashionapp.Interface.ClickItemProduct;
import com.example.fashionapp.Model.DataProduct;
import com.example.fashionapp.Model.Item;
import com.example.fashionapp.Model.Product;
import com.example.fashionapp.R;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WomenListProductActivity extends AppCompatActivity {
    private TextView mtextView,tvSoLuong;
    private ImageView imgBack;
    private RecyclerView mRecyclerView;
    private ListProductAdapter adapter;
    private ImageView imgGioHang;
    private LinearLayout layoutSapXep;
    private Disposable disposable;
    private NotificationBadge badge;

    private int selectedRadioButtonId = -1;
    private List<Product> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_women_list_product);
        AnhXa();
        onClickListener();
        Bundle bundle = getIntent().getExtras();
        if (bundle==null){
            return;
        }
        Item item = (Item) bundle.get( "item" );
        int pos = bundle.getInt( "ps" );
        mtextView.setText( item.getItem() ) ;
        //khoi tao Adapter
        GridLayoutManager gridLayoutManager= new GridLayoutManager( this,2 );
        mRecyclerView.setLayoutManager( gridLayoutManager );
        adapter.setData( getListProduct( pos ), new ClickItemProduct() {
            @Override
            public void onItemProductClick(Product product) {
                onClickgotoChitiet(product);
            }

            @Override
            public void onClickFavoriteItem(int pos) {

            }
        } );
        mRecyclerView.setAdapter( adapter );

        int itemCount = adapter.getItemCount();
        tvSoLuong.setText( Integer.toString(itemCount) );

    }

    private void AnhXa() {
        mtextView = findViewById( R.id.tv_id_item_men );
        mRecyclerView=findViewById( R.id.rcv_list_product );
        imgBack=findViewById( R.id.iv_back );
        imgGioHang=findViewById( R.id.img_gio_hang );
        tvSoLuong=findViewById( R.id.tv_so_luong );
        layoutSapXep=findViewById( R.id.linea_sap_xep );
        mList= new ArrayList<>();
        badge=findViewById( R.id.menu_sl );
        if (Utils.manggiohang!=null){
            badge.setText( String.valueOf( Utils.manggiohang.size() ) );
        }

        //khoi tao Adapter
        adapter = new ListProductAdapter( this );
    }
    private void onClickListener() {
        imgBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        imgGioHang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(WomenListProductActivity.this, GioHangActivity.class);
                startActivity( intent );
            }
        } );
        layoutSapXep.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSapXep();
            }
        } );
    }
    private void showDialogSapXep() {
        Dialog dialog = new Dialog( this );
        dialog.setContentView( R.layout.dialog_sap_xep );
        RadioGroup radioGroup =dialog.findViewById( R.id.radio_group );
        RadioButton radioTieuBieu= dialog.findViewById( R.id.radio_tieu_bieu );
        RadioButton radioCaoThap =dialog.findViewById( R.id.radio_cao_thap );
        RadioButton radioThapCao= dialog.findViewById( R.id.radio_thap_cao );
        radioTieuBieu.setChecked( true );
        radioThapCao.setChecked( selectedRadioButtonId == R.id.radio_thap_cao );
        radioCaoThap.setChecked( selectedRadioButtonId == R.id.radio_cao_thap );

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedRadioButtonId = checkedId;
                if (selectedRadioButtonId == R.id.radio_thap_cao) {
                    adapter.giathapcao();
                    dialog.dismiss();

                } else if (selectedRadioButtonId == R.id.radio_cao_thap) {
                    adapter.giaCaoThap();
                    dialog.dismiss();
                }else {
                    adapter.tieuBieu();
                    dialog.dismiss();
                }
            }
        });
        // Thiết lập vị trí dialog ở góc dưới màn hình
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // Chiều ngang dàn ra hết màn hình
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.BOTTOM; // Đặt dialog ở góc dưới màn hình
            window.setAttributes(layoutParams);
        }
        dialog.show();
    }

    private void onClickgotoChitiet(Product product) {
        Intent intent = new Intent(WomenListProductActivity.this, ChiTietProductActivity.class );
        Bundle bundle= new Bundle();
        bundle.putSerializable("object_product", product);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private List<Product> getListProduct(int pos) {
        List<Product> list = new ArrayList<>();
        switch (pos){
            case 0: {
                callApiAoWomen();

                break;
            }
            case 1: {
                callApiDoMacNgoai();
                break;
            }
            case 2:{
                callApiQuan();
                break;
            }
            case 3:{
                callApiVay();
                break;
            }
            case 4: {
                callApiBau();
                break;
            }
            case 5:{
                callApiDoLot();
                break;
            }
            case 6:{
                callApiDoMacNha();
                break;
            }
        }
        return list;
    }

    private void callApiDoMacNha() {
        APIService.apiService.getLisBauWomen().enqueue(new Callback<DataProduct>() {
            @Override
            public void onResponse(Call<DataProduct> call, Response<DataProduct> response) {
                if (response.body() == null) return;
                DataProduct data = response.body();
                mList = data.getData();
                adapter.setData( mList, new ClickItemProduct() {
                    @Override
                    public void onItemProductClick(Product product) {
                        onClickgotoChitiet(product);
                    }

                    @Override
                    public void onClickFavoriteItem(int pos) {

                    }
                } );
                mRecyclerView.setAdapter( adapter );

                int itemCount = adapter.getItemCount();
                tvSoLuong.setText( Integer.toString(itemCount) );
            }

            @Override
            public void onFailure(Call<DataProduct> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText( WomenListProductActivity.this, "Call api fall", Toast.LENGTH_SHORT ).show();

            }
        } );
    }

    private void callApiDoLot() {
        APIService.apiService.getLisLotWomen().enqueue( new Callback<DataProduct>() {
            @Override
            public void onResponse(Call<DataProduct> call, Response<DataProduct> response) {
                if (response.body() == null) return;
                DataProduct data = response.body();
                mList = data.getData();
                adapter.setData( mList, new ClickItemProduct() {
                    @Override
                    public void onItemProductClick(Product product) {
                        onClickgotoChitiet(product);
                    }

                    @Override
                    public void onClickFavoriteItem(int pos) {

                    }
                } );
                mRecyclerView.setAdapter( adapter );

                int itemCount = adapter.getItemCount();
                tvSoLuong.setText( Integer.toString(itemCount) );
            }

            @Override
            public void onFailure(Call<DataProduct> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText( WomenListProductActivity.this, "Call api fall", Toast.LENGTH_SHORT ).show();

            }
        } );
    }

    private void callApiBau() {
        APIService.apiService.getLisBauWomen().enqueue( new Callback<DataProduct>() {
            @Override
            public void onResponse(Call<DataProduct> call, Response<DataProduct> response) {
                if (response.body() == null) return;
                DataProduct data = response.body();
                mList = data.getData();
                adapter.setData( mList, new ClickItemProduct() {
                    @Override
                    public void onItemProductClick(Product product) {
                        onClickgotoChitiet(product);
                    }

                    @Override
                    public void onClickFavoriteItem(int pos) {

                    }
                } );
                mRecyclerView.setAdapter( adapter );

                int itemCount = adapter.getItemCount();
                tvSoLuong.setText( Integer.toString(itemCount) );
            }

            @Override
            public void onFailure(Call<DataProduct> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText( WomenListProductActivity.this, "Call api fall", Toast.LENGTH_SHORT ).show();

            }
        } );
    }

    private void callApiVay() {
        APIService.apiService.getLisVayWomen().enqueue( new Callback<DataProduct>() {
            @Override
            public void onResponse(Call<DataProduct> call, Response<DataProduct> response) {
                if (response.body() == null) return;
                DataProduct data = response.body();
                mList = data.getData();
                adapter.setData( mList, new ClickItemProduct() {
                    @Override
                    public void onItemProductClick(Product product) {
                        onClickgotoChitiet(product);
                    }

                    @Override
                    public void onClickFavoriteItem(int pos) {

                    }
                } );
                mRecyclerView.setAdapter( adapter );

                int itemCount = adapter.getItemCount();
                tvSoLuong.setText( Integer.toString(itemCount) );
            }

            @Override
            public void onFailure(Call<DataProduct> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText( WomenListProductActivity.this, "Call api fall", Toast.LENGTH_SHORT ).show();

            }
        } );
    }

    private void callApiQuan() {
        APIService.apiServiceNu.getLisQuanWomen().enqueue( new Callback<DataProduct>() {
            @Override
            public void onResponse(Call<DataProduct> call, Response<DataProduct> response) {
                if (response.body() == null) return;
                DataProduct data = response.body();
                mList = data.getData();
                adapter.setData( mList, new ClickItemProduct() {
                    @Override
                    public void onItemProductClick(Product product) {
                        onClickgotoChitiet(product);
                    }

                    @Override
                    public void onClickFavoriteItem(int pos) {

                    }
                } );
                mRecyclerView.setAdapter( adapter );

                int itemCount = adapter.getItemCount();
                tvSoLuong.setText( Integer.toString(itemCount) );
            }

            @Override
            public void onFailure(Call<DataProduct> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText( WomenListProductActivity.this, "Call api fall", Toast.LENGTH_SHORT ).show();

            }
        } );
    }

    private void callApiDoMacNgoai() {
        APIService.apiServiceNu.getLisDoMacNgoaiWomem().enqueue( new Callback<DataProduct>() {
            @Override
            public void onResponse(Call<DataProduct> call, Response<DataProduct> response) {
                if (response.body() == null) return;
                DataProduct data = response.body();
                mList = data.getData();
                adapter.setData( mList, new ClickItemProduct() {
                    @Override
                    public void onItemProductClick(Product product) {
                        onClickgotoChitiet(product);
                    }

                    @Override
                    public void onClickFavoriteItem(int pos) {

                    }
                } );
                mRecyclerView.setAdapter( adapter );

                int itemCount = adapter.getItemCount();
                tvSoLuong.setText( Integer.toString(itemCount) );
            }

            @Override
            public void onFailure(Call<DataProduct> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText( WomenListProductActivity.this, "Call api fall", Toast.LENGTH_SHORT ).show();

            }
        } );
    }

    private void callApiAoWomen() {
        APIService.apiServiceNu.getLisAoWomen().enqueue( new Callback<DataProduct>() {
            @Override
            public void onResponse(Call<DataProduct> call, Response<DataProduct> response) {
                if (response.body() == null) return;
                DataProduct data = response.body();
                mList = data.getData();
                adapter.setData( mList, new ClickItemProduct() {
                    @Override
                    public void onItemProductClick(Product product) {
                        onClickgotoChitiet(product);
                    }

                    @Override
                    public void onClickFavoriteItem(int pos) {

                    }
                } );
                mRecyclerView.setAdapter( adapter );

                int itemCount = adapter.getItemCount();
                tvSoLuong.setText( Integer.toString(itemCount) );
            }

            @Override
            public void onFailure(Call<DataProduct> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText( WomenListProductActivity.this, "Call api fall", Toast.LENGTH_SHORT ).show();

            }
        } );
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable!=null){
            disposable.dispose();
        }
    }


}