package com.example.fashionapp.Activity;

import androidx.appcompat.app.AlertDialog;
import com.example.fashionapp.Activity.ThanhToanActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fashionapp.API.Utils;
import com.example.fashionapp.Adapter.GioHangAdapter;
import com.example.fashionapp.Interface.ClickCongTruGioHang;
import com.example.fashionapp.Model.TinhTongEvent;
import com.example.fashionapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class GioHangActivity extends AppCompatActivity implements ClickCongTruGioHang {
    private ImageView imgBack;
    private RecyclerView recyclerView;
    private TextView tvSoLuong,tvTongCong,tvTongDon, tvThanhToan;
    private Button btnVoCher,btnThanhToan;
    private GioHangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        AnhXa();
        //khoi tao adpter
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GioHangAdapter(getApplicationContext(), Utils.manggiohang, this);
        recyclerView.setAdapter(adapter);
        //phân ngang giưax các item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration( recyclerView.getContext(), ((LinearLayoutManager) layoutManager).getOrientation() );
        recyclerView.addItemDecoration( dividerItemDecoration );


        tinhTongTien();
        onClickListener();
    }

    private void AnhXa() {
        imgBack=findViewById( R.id.iv_back );
        recyclerView=findViewById( R.id.rcv_gio_hang );
        tvSoLuong=findViewById( R.id.tv_so_luong );
        tvTongCong=findViewById( R.id.tv_tong_cong );
        tvTongDon=findViewById( R.id.tv_tongdon );
        tvThanhToan=findViewById( R.id.tv_tong_than_toan );
        btnThanhToan=findViewById( R.id.btn_thanh_toan );
    }
    private void onClickListener() {
        imgBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        if (adapter.getItemCount()>0){
            btnThanhToan.setVisibility( View.VISIBLE );
            btnThanhToan.setOnClickListener( v->{
                Intent intent = new Intent(GioHangActivity.this, ThanhToanActivity.class);
                startActivity( intent );
            } );


        }else {
            btnThanhToan.setVisibility( View.GONE );
        }
    }
    private void tinhTongTien() {
        if (Utils.manggiohang != null) {
            int sum = 0;
            int totalItem = 0;
            for (int i = 0; i < Utils.manggiohang.size(); i++) {
                sum += Utils.manggiohang.get( i ).getPrice() * Utils.manggiohang.get( i ).getSoluong();
                totalItem = totalItem + Utils.manggiohang.get( i ).getSoluong();
            }
            DecimalFormat decimalFormat = new DecimalFormat( "###,###,###" );
            tvThanhToan.setText( decimalFormat.format( sum ) + " VND" );
            tvTongDon.setText( decimalFormat.format( sum ) + " VND" );
            tvTongCong.setText( decimalFormat.format( sum ) + " VND" );
            tvSoLuong.setText( String.valueOf( totalItem ) );
        }
    }

    @Override
    public void onImageClick( int position, int giatri) {
        if (giatri == 1){
            if (Utils.manggiohang.get( position ).getSoluong()<11) {
                int soluongmoi = Utils.manggiohang.get( position ).getSoluong() + 1;
                Utils.manggiohang.get( position ).setSoluong( soluongmoi );
            }
        } else if (giatri == 2){
            if (Utils.manggiohang.get( position ).getSoluong()>1) {
                int soluongmoi = Utils.manggiohang.get( position ).getSoluong() - 1;
                Utils.manggiohang.get( position ).setSoluong( soluongmoi );
            }
        }
        adapter.notifyDataSetChanged();
        EventBus.getDefault().postSticky( new TinhTongEvent() );
        tinhTongTien();
    }

    @Override
    public void onDelete(int pos) {
        AlertDialog.Builder  dialog = new AlertDialog.Builder(this);
        dialog.setTitle( "Xác nhận xóa" );
        dialog.setMessage( "Bạn có chắc muốn xóa?" );
        dialog.setPositiveButton( "Oke", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.manggiohang.remove( pos );
                        adapter.notifyDataSetChanged();
                        tinhTongTien();
                    }
                } )
                .setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                } );
        dialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register( this );
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister( this );
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void EventTinhTien(TinhTongEvent event){
        if (event!=null){
            tinhTongTien();
        }
    }
}