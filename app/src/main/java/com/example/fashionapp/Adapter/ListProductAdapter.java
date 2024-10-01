package com.example.fashionapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.example.fashionapp.dataLocal.Database;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ListProductViewHolder> {
    private Context mContext;
    private List<Product> mproList;

    private ClickItemProduct clickItemProduct;
    private boolean isFavorite=false ;

    // Tạo bản sao của danh sách mục ban đầu
    List<Product> originalItems ;

    public ListProductAdapter(Context mContext) {
        this.mContext = mContext;
       this.originalItems=new ArrayList<>();
    }

    public void setData(List<Product> list, ClickItemProduct listener){
        this.mproList=list;
        this.clickItemProduct=listener;
        this.originalItems=new ArrayList<>(mproList);
        notifyDataSetChanged();
    }

    public void giathapcao() {
        Collections.sort(mproList, new Comparator<Product>() {
            @Override
            public int compare(Product item1, Product item2) {
                return Double.compare(item1.getPrice(), item2.getPrice());
            }
        });
        notifyDataSetChanged();
    }
    public void giaCaoThap(){
        Collections.sort( mproList, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return Double.compare( o2.getPrice(),o1.getPrice() );
            }
        } );
        notifyDataSetChanged();
    }
    public void  tieuBieu(){
        // Gán lại danh sách mục từ bản sao ban đầu vào danh sách hiện tại
        mproList.clear();
        mproList.addAll(originalItems);

        // Cập nhật RecyclerView
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ListProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(  parent.getContext()).inflate( R.layout.item_list_product,parent,false );

        return new ListProductViewHolder( view );
    }


    @Override

    public void onBindViewHolder(@NonNull ListProductViewHolder holder, int position) {
        Product product = mproList.get(position);
        if (product == null) {
            return;
        }

        Glide.with(mContext).load(product.getImage()).into(holder.imgListProduct);
        holder.tvSex.setText(product.getGender());
        holder.tvNameProduct.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvgiaProduct.setText(decimalFormat.format(product.getPrice()) + " VND");
        holder.tvMaSP.setText(String.valueOf(product.getProduct_id()));
        int giaSale = product.getPrice() - (product.getPrice() * product.getSale() / 100);
        if (product.getSale() == 0) {
            holder.tvSale.setVisibility(View.GONE);
            holder.tvphamtramSale.setVisibility( View.GONE );
            // Xóa gạch ngang khi không có giá sale
            holder.tvgiaProduct.setPaintFlags(holder.tvgiaProduct.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvgiaProduct.setTextColor( Color.BLACK);

        } else {
            holder.tvSale.setVisibility(View.VISIBLE);
            holder.tvSale.setText(decimalFormat.format(giaSale) + " VND");
            holder.tvphamtramSale.setVisibility( View.VISIBLE );
            holder.tvphamtramSale.setText("-"+ decimalFormat.format( product.getSale() )+"%" );
            // Gạch ngang tvgiaProduct
            holder.tvgiaProduct.setPaintFlags(holder.tvgiaProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvgiaProduct.setTextColor(Color.GRAY);

        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickItemProduct.onItemProductClick(product);
            }
        });
        // Thiết lập hình ảnh yêu thích tương ứng với trạng thái
        List<Product> favoriteProducts = Database.getInstance( mContext ).favouriteDAO().getListFavourite();
        if (favoriteProducts.contains(product)) {
            isFavorite = true;
            holder.imgFavourite.setImageResource(R.drawable.ic_read_favorite);
        } else {
            isFavorite = false;
            holder.imgFavourite.setImageResource(R.drawable.baseline_favorite_24);
        }
        holder.imgFavourite.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    holder.imgFavourite.setImageResource(R.drawable.baseline_favorite_24);
                    // Xử lý gỡ khỏi yêu thích
                    Database.getInstance(mContext).favouriteDAO().deleteFavourite(product);
                } else {
                    List<Product> favoriteProducts = Database.getInstance(mContext).favouriteDAO().getListFavourite();
                    if (favoriteProducts.contains(product)) {
                        // Product đã tồn tại trong danh sách yêu thích
                        // Thực hiện các hành động cập nhật sản phẩm hoặc hiển thị thông báo
                        notifyDataSetChanged();
                        // Ví dụ: Toast.makeText(mContext, "Sản phẩm đã tồn tại trong danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    } else {
                        holder.imgFavourite.setImageResource(R.drawable.ic_read_favorite);
                        // Xử lý thêm vào yêu thích
                        Database.getInstance(mContext).favouriteDAO().insertFavourite(product);
                    }
                }
                isFavorite = !isFavorite;
            }
        });
    }


    @Override
    public int getItemCount() {
        if (mproList!=null){
            return mproList.size();
        }
        return 0;
    }

    public class ListProductViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgListProduct;
        private TextView tvSex,tvSize,tvNameProduct,tvgiaProduct,tvMaSP,tvSale,tvphamtramSale;
        private ImageView imgFavourite;
        private CardView cardView;

        public ListProductViewHolder(@NonNull View itemView) {
            super( itemView );
            imgListProduct=itemView.findViewById( R.id.img_list_product );
            tvSex=itemView.findViewById( R.id.tv_list_sex );
            tvSize=itemView.findViewById( R.id.tv_list_size );
            tvNameProduct=itemView.findViewById( R.id.tv_list_name_product );
            tvgiaProduct=itemView.findViewById( R.id.tv_list_cost_product );
            imgFavourite=itemView.findViewById( R.id.icon_list_favorite );
            tvMaSP=itemView.findViewById( R.id.tv_ma_sp );
            tvSale=itemView.findViewById( R.id.tv_cost_sale );
            cardView=itemView.findViewById( R.id.card_view_product );
            tvphamtramSale=itemView.findViewById( R.id.tv_sale );

        }
    }
}