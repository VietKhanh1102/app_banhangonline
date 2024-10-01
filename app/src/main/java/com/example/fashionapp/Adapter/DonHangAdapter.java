package com.example.fashionapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fashionapp.Model.Thanhtoan;
import com.example.fashionapp.R;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.DonHangViewHolder> {
    private RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
    private List<Thanhtoan> list;
    private Context context;
    private onClick onClick;

    public interface onClick {
        void onClick(Thanhtoan thanhtoan);
    }

    public DonHangAdapter(List<Thanhtoan> list, Context context, onClick onClick) {
        this.list = list;
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public DonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_hang, parent, false);
        return new DonHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangViewHolder holder, int position) {
        Thanhtoan thanhtoan = list.get(position);
        if (thanhtoan == null) {
            return;
        }
        holder.tvDonHang.setText("Đơn Hàng Số: " + thanhtoan.getOrder_id() + " ");

        // Kiểm tra onClick trước khi sử dụng
        if (onClick != null) {
            holder.cvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onClick(thanhtoan);
                }
            });
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.recyclerChitiet.getContext(), LinearLayoutManager.VERTICAL, false
        );
        linearLayoutManager.setInitialPrefetchItemCount(thanhtoan.getItem().size());

        ChiTietDonHangAdapter chiTietDonHangAdapter = new ChiTietDonHangAdapter(thanhtoan.getItem(), context);
        holder.recyclerChitiet.setLayoutManager(linearLayoutManager);
        holder.recyclerChitiet.setAdapter(chiTietDonHangAdapter);
        holder.recyclerChitiet.setRecycledViewPool(pool);

        // Hiển thị trạng thái đơn hàng
        holder.ttDonhang.setText(getStatusString(thanhtoan.getTrangthai()));
    }



    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class DonHangViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDonHang, ttDonhang;
        private RecyclerView recyclerChitiet;
        private CardView cvDelete;

        public DonHangViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDonHang = itemView.findViewById(R.id.tv_don_hang);
            recyclerChitiet = itemView.findViewById(R.id.Recycler_chitiet);
            cvDelete = itemView.findViewById(R.id.cv_delete);
            ttDonhang = itemView.findViewById(R.id.tt_donhang);
        }
    }

    // Hàm chuyển trạng thái số thành chuỗi
    private String getStatusString(int status) {
        switch (status) {
            case 0:
                return "Đang xử lý";
            case 1:
                return "Đơn hàng đã được chấp nhận";
            case 2:
                return "Đơn hàng đã giao cho đơn vị vận chuyển";
            case 3:
                return "Hoàn thành";
            case 4:
                return "Đơn hàng đã huỷ";
            default:
                return "Không xác định";
        }
    }
}
