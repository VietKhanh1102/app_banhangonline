package com.example.fashionapp.Adapter;

import android.annotation.SuppressLint;
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
import com.example.fashionapp.Interface.ClickItemProduct;
import com.example.fashionapp.Model.Product;
import com.example.fashionapp.R;

import java.text.DecimalFormat;
import java.util.List;

public class SanPhamNoiBatAdapter extends RecyclerView.Adapter<SanPhamNoiBatAdapter.ListProductViewHolder>{
    private Context mContext;
    private List<Product> mproList;

    private ClickItemProduct clickItemProduct;

    public SanPhamNoiBatAdapter(Context mContext) {
        this.mContext = mContext;

    }

    public void setData(List<Product> list, ClickItemProduct listener){
        this.mproList=list;
        this.clickItemProduct=listener;

        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SanPhamNoiBatAdapter.ListProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(  parent.getContext()).inflate( R.layout.item_sanpham_noibat,parent,false );

        return new SanPhamNoiBatAdapter.ListProductViewHolder( view );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SanPhamNoiBatAdapter.ListProductViewHolder holder, int position) {
        Product product = mproList.get( position );
        if (product ==null){
            return;
        }

        Glide.with( mContext ).load( product.getImage() ).into( holder.imgListProduct );

        holder.tvNameProduct.setText( product.getName() );
        DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
        holder.tvgiaProduct.setText( decimalFormat.format (product.getPrice())+ " VND" );
        // Kiểm tra xem mục hiện tại có phải là mục được chọn không
        holder.cardView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickItemProduct.onItemProductClick( product );
            }
        } );
    }
    @Override
    public int getItemCount() {
        if (mproList!=null){
            return Math.min(mproList.size(), 9);
        }
        return 0;
    }

    public class ListProductViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgListProduct;
        private TextView tvSex,tvSize,tvNameProduct,tvgiaProduct,tvMaSP;
        private ImageView imgFavourite;
        private CardView cardView;

        public ListProductViewHolder(@NonNull View itemView) {
            super( itemView );
            imgListProduct=itemView.findViewById( R.id.img_list_product );
            tvSize=itemView.findViewById( R.id.tv_list_size );
            tvNameProduct=itemView.findViewById( R.id.tv_list_name_product );
            tvgiaProduct=itemView.findViewById( R.id.tv_list_cost_product );
            imgFavourite=itemView.findViewById( R.id.icon_list_favorite );

            cardView=itemView.findViewById( R.id.card_view_product );

        }
    }
}
