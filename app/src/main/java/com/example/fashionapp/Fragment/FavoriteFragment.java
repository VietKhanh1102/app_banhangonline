package com.example.fashionapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fashionapp.API.Utils;
import com.example.fashionapp.Activity.ChiTietProductActivity;
import com.example.fashionapp.Activity.GioHangActivity;
import com.example.fashionapp.Adapter.FavouriteAdapter;
import com.example.fashionapp.Interface.ClickItemProduct;
import com.example.fashionapp.Model.Product;
import com.example.fashionapp.R;
import com.example.fashionapp.dataLocal.Database;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private ImageView imgGioHang, imgNen,imvChinhSua ;
    private TextView tvSoLuong;
    private RecyclerView mRecyclerView;
    private View mView;
    private FavouriteAdapter mItemAdapter;
    private NotificationBadge badge;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Product> items = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_favorite, container, false);
        AnhXa();

        mItemAdapter = new FavouriteAdapter( items );
        mRecyclerView.setAdapter(mItemAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // Cập nhật dữ liệu cho adapter từ FavoriteProductsManager
        items= Database.getInstance( getActivity() ).favouriteDAO().getListFavourite();
        mItemAdapter.setData( items, new ClickItemProduct() {
            @Override
            public void onItemProductClick(Product product) {
                onClickgotoChitiet( product );
            }

            @Override
            public void onClickFavoriteItem(int pos) {

            }
        } );
        if (mItemAdapter != null && mItemAdapter.getItemCount() == 0) {
            imgNen.setVisibility(View.VISIBLE);

        } else {
            imgNen.setVisibility(View.GONE);
        }
        tvSoLuong.setText( Integer.toString(mItemAdapter.getItemCount()) );

        onClickList();
        swipeRefreshLayout.setOnRefreshListener( this );
        return mView;
    }

    private void AnhXa() {
        imgGioHang=mView.findViewById( R.id.img_gio_hang );
        imvChinhSua = mView.findViewById(R.id.imv_chinh_sua);
        imgNen=mView.findViewById( R.id.img_nen );
        mRecyclerView = mView.findViewById( R.id.rcv_list_favourite );
        tvSoLuong=mView.findViewById( R.id.tv_so_luong );
        badge=mView.findViewById( R.id.menu_sl );
        swipeRefreshLayout=mView.findViewById( R.id.swiperefresh );
        if (Utils.manggiohang!=null){
            badge.setText( String.valueOf( Utils.manggiohang.size() ) );
        }
    }

    private void onClickList() {
        imgGioHang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(requireActivity(), GioHangActivity.class);
                startActivity( intent );
            }
        } );
    }

    private void onClickgotoChitiet(Product product) {
        Intent intent = new Intent( requireActivity(), ChiTietProductActivity.class );
        Bundle bundle= new Bundle();
        bundle.putSerializable("object_product", product);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
//
    }

    @Override
    public void onRefresh() {
        // Cập nhật dữ liệu cho adapter từ FavoriteProductsManager
        items= Database.getInstance( getActivity() ).favouriteDAO().getListFavourite();
        mItemAdapter.setData( items, new ClickItemProduct() {
            @Override
            public void onItemProductClick(Product product) {
                onClickgotoChitiet( product );
            }

            @Override
            public void onClickFavoriteItem(int pos) {

            }
        } );
        if (mItemAdapter != null && mItemAdapter.getItemCount() == 0) {
            imgNen.setVisibility(View.VISIBLE);

        } else {
            imgNen.setVisibility(View.GONE);
        }
        tvSoLuong.setText( Integer.toString(mItemAdapter.getItemCount()) );
        Handler handler = new Handler();
        handler.postDelayed( new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing( false );
            }
        },2000 );

    }



}