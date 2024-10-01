package com.example.fashionapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fashionapp.Model.GioHang;
import com.example.fashionapp.R;

import java.util.List;

public class ThanhToanAdapter extends RecyclerView.Adapter<ThanhToanAdapter.ThanhToanViewHolder> {
    private Context context;
    private List<GioHang> gioHangList;

    public ThanhToanAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public ThanhToanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_product_oder,parent,false );
        return new ThanhToanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThanhToanViewHolder holder, int position) {
        GioHang gioHang= gioHangList.get( position );
        Glide.with( context ).load( gioHang.getImage() ).into( holder.imgProduct );
        holder.tvSoLuong.setText( gioHang.getSoluong() +" ");
    }

    @Override
    public int getItemCount() {
        if (gioHangList!=null){
            return gioHangList.size();
        }
        return 0;
    }

    public class ThanhToanViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView tvSoLuong;
        public ThanhToanViewHolder(@NonNull View itemView) {
            super( itemView );
            imgProduct=itemView.findViewById( R.id.imv_product );
            tvSoLuong=itemView.findViewById( R.id.tv_so_luong );
        }
    }
}
