package com.example.motogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.OrderHistoryViewHolder> {

    private List<OrderForHistory> orderHistory;
    private Context context;
    private int selectedPosition = RecyclerView.NO_POSITION; // Biến để lưu vị trí item được chọn

    public HistoryAdapter(Context context, List<OrderForHistory> orderHistory) {
        this.context = context;
        this.orderHistory = orderHistory;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);  // Có thể sử dụng layout riêng cho lịch sử
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        int currentPosition = holder.getAdapterPosition(); // Lấy vị trí mới nhất
        if (currentPosition == RecyclerView.NO_POSITION) return; // Kiểm tra nếu vị trí không hợp lệ

        OrderForHistory orderItem = orderHistory.get(currentPosition);
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

    @Override
    public int getItemCount() {
        return orderHistory.size();
    }

    // Lấy đơn hàng đã chọn
    public OrderForHistory getSelectedOrder() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            return orderHistory.get(selectedPosition);
        }
        return null;
    }

    public static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView SoCCCD, TenKH, NgayTra, NgayThue, SoHopDong, SoDT, ViTri, TongGiaTien;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            SoCCCD = itemView.findViewById(R.id.txtSoCCCD);
            SoHopDong = itemView.findViewById(R.id.txtSoHopDong);
            TongGiaTien = itemView.findViewById(R.id.txtTongGiaTien);
            TenKH = itemView.findViewById(R.id.txtTenKH);
            NgayTra = itemView.findViewById(R.id.txtNgayTra);
            NgayThue = itemView.findViewById(R.id.txtNgayThue);
            SoDT = itemView.findViewById(R.id.txtSoDT);
            ViTri = itemView.findViewById(R.id.txtViTriGiaoXe);
        }
    }
}