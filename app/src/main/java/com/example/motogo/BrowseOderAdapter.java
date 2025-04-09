package com.example.motogo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
public class BrowseOderAdapter extends RecyclerView.Adapter<BrowseOderAdapter.OrderViewHolder> {

    private List<OrderForBrowse> order;
    private Context context;
    private int selectedPosition = RecyclerView.NO_POSITION; // Biến để lưu vị trí item được chọn

    public BrowseOderAdapter(Context context , List<OrderForBrowse> order){
        this.context = context;
        this.order = order;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        int currentPosition = holder.getAdapterPosition(); // Lấy vị trí mới nhất
        if (currentPosition == RecyclerView.NO_POSITION) return; // Kiểm tra nếu vị trí không hợp lệ

        OrderForBrowse orderItem = order.get(currentPosition);
        holder.SoCCCD.setText("CCCD: " + orderItem.getSoCCCD());
        holder.TenKH.setText("Tên: " + orderItem.getTenKH());
        holder.NgayTra.setText("Ngày Trả: " + orderItem.getNgayTra());
        holder.NgayThue.setText("Ngày Thuê: " + orderItem.getNgayThue());
        holder.SoHopDong.setText("Số HD: " + orderItem.getSoHopDong());
        holder.SoDT.setText("SDT: " + orderItem.getSoDT());
        holder.ViTri.setText("Vị Trí Giao Xe: " + orderItem.getViTriGiaoXe());
        holder.TongGiaTien.setText("Tổng Chi Phí: " + orderItem.getTongGiaTien());

        // Kiểm tra nếu item này được chọn, thay đổi màu nền
        if (currentPosition == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.selected_border); // Đặt viền xanh nhạt
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white)); // Màu bình thường
        }

        // Xử lý khi người dùng bấm vào item
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition(); // Lấy lại vị trí mới nhất khi click
            notifyDataSetChanged(); // Refresh lại danh sách để cập nhật màu nền
        });
    }

    public int getItemCount() {
        return order.size();
    }

    // Lấy đơn hàng đã chọn
    public OrderForBrowse getSelectedOrder() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            return order.get(selectedPosition);
        }
        return null;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder{

        TextView SoCCCD, TenKH, NgayTra, NgayThue, SoHopDong, SoDT, ViTri, TongGiaTien;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            SoCCCD = itemView.findViewById(R.id.txtSoCCCD);
            SoHopDong =  itemView.findViewById(R.id.txtSoHopDong);
            TongGiaTien =  itemView.findViewById(R.id.txtTongGiaTien);
            TenKH =  itemView.findViewById(R.id.txtTenKH);
            NgayTra =  itemView.findViewById(R.id.txtNgayTra);
            NgayThue =  itemView.findViewById(R.id.txtNgayThue);
            SoDT =  itemView.findViewById(R.id.txtSoDT);
            ViTri =  itemView.findViewById(R.id.txtViTriGiaoXe);
        }
    }
}
