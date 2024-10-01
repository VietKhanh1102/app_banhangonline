package com.example.fashionapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fashionapp.Interface.ClickCongTruGioHang;
import com.example.fashionapp.Model.GioHang;
import com.example.fashionapp.R;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.GioHangViewHolder> {
    private Context context;
    private List<GioHang> gioHangList;

    private ClickCongTruGioHang clickCongTruGioHang;

    public GioHangAdapter(Context context, List<GioHang> gioHangList, ClickCongTruGioHang listener) {
        this.context = context;
        this.gioHangList = gioHangList;
        this.clickCongTruGioHang = listener;

    }

    @NonNull
    @Override
    public GioHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_gio_hang,parent,false );
        return new GioHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangViewHolder holder, int position) {
        GioHang gioHang= gioHangList.get( position );
        Glide.with( context ).load( gioHang.getImage() ).into( holder.imgProduct );
        DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
        holder.tvGia.setText( decimalFormat.format (gioHang.getPrice())+ " VND" );
        holder.tvSoLuong.setText( gioHang.getSoluong() +" ");
        holder.tvSize.setText( gioHang.getKichco() );
        int gia = gioHang.getSoluong()*gioHang.getPrice();
        holder.tvTongGia.setText( decimalFormat.format( gia )+" VND" );
        holder.tvTenProduct.setText( gioHang.getName() );
        holder.tvIdProduct.setText( gioHang.getProduct_id() +" " );
        holder.tvGioiTinh.setText( gioHang.getGender() );

        holder.imvRemove.setOnClickListener( v -> {
            clickCongTruGioHang.onDelete( position );
        } );
        holder.imvCong.setOnClickListener( v -> {
            clickCongTruGioHang.onImageClick( position ,1 );
        } );
        holder.imvTru.setOnClickListener( v -> {
            clickCongTruGioHang.onImageClick( position ,2 );
        } );

    }

    @Override
    public int getItemCount() {
        if (gioHangList!=null){
            return gioHangList.size();
        }
        return 0;
    }


    public class GioHangViewHolder extends RecyclerView.ViewHolder  {
        private ImageView imgProduct, imvCong, imvTru, imvRemove;
        private TextView tvTenProduct, tvSize,tvSoLuong,tvGia, tvTongGia, tvIdProduct, tvGioiTinh;

        private CardView cardView;


        public GioHangViewHolder(@NonNull View itemView) {
            super( itemView );
            imgProduct=itemView.findViewById( R.id.img_item_giohang );
            imvCong=itemView.findViewById( R.id.img_giohang_cong );
            imvTru=itemView.findViewById( R.id.img_giohang_tru );
            tvTenProduct=itemView.findViewById( R.id.tv_gio_hang_product );
            tvSize=itemView.findViewById( R.id.tv_gio_hang_size );
            tvGia=itemView.findViewById( R.id.tv_gia_item );
            tvSoLuong=itemView.findViewById( R.id.tv_so_luong );
            tvTongGia=itemView.findViewById( R.id.tv_tong_gia );
            tvIdProduct=itemView.findViewById( R.id.tv_ma_product );
            tvGioiTinh=itemView.findViewById( R.id.tv_gioi_tinh );

            imvRemove=itemView.findViewById( R.id.img_remove );
            cardView=itemView.findViewById( R.id.card_view );


        }
    }
}
