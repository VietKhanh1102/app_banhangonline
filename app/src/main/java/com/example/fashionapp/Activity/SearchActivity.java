package com.example.fashionapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fashionapp.Adapter.ListProductAdapter;
import com.example.fashionapp.Fragment.SearchFramgent;
import com.example.fashionapp.Interface.APIService;
import com.example.fashionapp.Interface.ClickItemProduct;
import com.example.fashionapp.Model.DataProduct;
import com.example.fashionapp.Model.Product;
import com.example.fashionapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private AutoCompleteTextView autoSearch;
    private ImageView imgBack;
    private RecyclerView recyclerView;
    private TextView tvSoLuong;
    private LinearLayout linearLayout;
    private ListProductAdapter productAdapter;
    private List<Product> productList;
    private SearchFramgent searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        AnhXa();
        clickListen();
    }
    private void AnhXa() {
        productList = new ArrayList<>();
        autoSearch=findViewById( R.id.auto_search );
        imgBack=findViewById( R.id.img_back );
        tvSoLuong=findViewById( R.id.tv_so_luong );
        linearLayout=findViewById( R.id.linea_sap_xep );
        recyclerView=findViewById( R.id.recy_search);
        //  LinearLayoutManager layoutManager= new LinearLayoutManager( this );
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager( 2,StaggeredGridLayoutManager.VERTICAL );
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager( gridLayoutManager );
    }

    private void clickListen() {
        linearLayout.setOnClickListener( v->{
            FragmentManager fragmentManager = SearchActivity.this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            searchFragment = new SearchFramgent();
            fragmentTransaction.replace(R.id.activity_search, searchFragment ); // R.id.container là ID của container layout để thay thế Fragment
            fragmentTransaction.addToBackStack(null); // Nếu bạn muốn Fragment mới được thêm vào stack back, bạn có thể sử dụng addToBackStack()
            fragmentTransaction.commit();
        } );
        imgBack.setOnClickListener( v->{
            onBackPressed();
        } );
        String[] searchKeywords = {"Áo","Áo thun","Áo polo" ,"quần","Quần","Quần jean","Quần âu", "Quần Leggings","Quần short","Đầm bầu", "Váy ngắn","Đầm"};
        // Tạo Adapter để cung cấp dữ liệu gợi ý
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.item_search_auto_complete, searchKeywords);
        // Đặt Adapter cho AutoCompleteTextView
        autoSearch.setAdapter(adapter);
        // Xử lý sự kiện tìm kiếm
        autoSearch.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()==0){
                    productList.clear();
                    productAdapter = new ListProductAdapter( getApplicationContext() );
                    recyclerView.setAdapter( productAdapter );
                }else {
                    getDataSearch( s.toString() );
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        } );

    }
    private void getDataSearch(String s) {
        productList.clear();

        APIService.apiServiceSearch.search(s).enqueue(new Callback<DataProduct>() {
            @Override
            public void onResponse(Call<DataProduct> call, Response<DataProduct> response) {
                if (response.isSuccessful()) {
                    DataProduct data = response.body();
                    if (data != null) {
                        productAdapter = new ListProductAdapter( getApplicationContext() );
                        productList = data.getData();
                        productAdapter.setData(productList, new ClickItemProduct() {
                            @Override
                            public void onItemProductClick(Product product) {
                                onClickgotoChitiet(product);
                            }

                            @Override
                            public void onClickFavoriteItem(int pos) {
                                // Handle favorite item click event
                            }
                        });
                        recyclerView.setAdapter(productAdapter);
                        int itemCount = productAdapter.getItemCount();
                        tvSoLuong.setText( Integer.toString(itemCount) );
                    }
                } else {
                    // Handle unsuccessful response
                    productList.clear();

                    productAdapter.notifyDataSetChanged();
                    Toast.makeText(SearchActivity.this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataProduct> call, Throwable t) {
                // Handle failure
                t.printStackTrace();
                Toast.makeText(SearchActivity.this, "call ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickgotoChitiet(Product product) {
        Intent intent = new Intent( SearchActivity.this, ChiTietProductActivity.class );
        Bundle bundle= new Bundle();
        bundle.putSerializable("object_product", product);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}