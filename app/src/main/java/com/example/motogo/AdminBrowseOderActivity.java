package com.example.motogo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AdminBrowseOderActivity extends AppCompatActivity {

    private ImageButton btnMenu, btnCloseMenu;
    private Button  btnMenuOder, btnMenuHistory;
    private LinearLayout Menu;
    private List<OrderForBrowse> order = new ArrayList<>();
    private List<OrderForHistory> hisOrder = new ArrayList<>();
    private Button btnDuyet;
    private BrowseOderAdapter browseOder;
    private HistoryAdapter historyAdapter;
    private RecyclerView recyclerView;
    private TextView txtTittle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_browse_oder);

        CreateView();

        //setting menu
        btnMenu.setOnClickListener(v -> OpenMenu());
        btnCloseMenu.setOnClickListener(v -> CloseMenu());
        btnMenuOder.setEnabled(false);
        btnMenuHistory.setOnClickListener(v -> ViewHistory());
        btnMenuOder.setOnClickListener(v -> ViewOrder());



        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        LoadData();



        btnDuyet.setOnClickListener(v -> Browse());


    }


    private void CreateView(){
        btnMenu = findViewById(R.id.btnMenu);
        Menu = findViewById(R.id.MenuMain);
        btnCloseMenu = findViewById(R.id.btnCloseMenu);
        btnMenuHistory = findViewById(R.id.btnMenuHistory);
        btnMenuOder = findViewById(R.id.btnMenuOder);
        recyclerView = findViewById(R.id.ShowInfo);
        btnDuyet = findViewById(R.id.btnDuyet);
        txtTittle = findViewById(R.id.Tittle2);
    }

    private void Browse(){
        OrderForBrowse selectedOrder = browseOder.getSelectedOrder();
        if (selectedOrder != null) {
            saveOrderToHistory(selectedOrder);

            String contractNumber = selectedOrder.getSoHopDong(); // Giả sử có phương thức này

            // Xóa đơn hàng khỏi danh sách dữ liệu
            removeOrderFromPreferences(selectedOrder);

            // Cập nhật danh sách trong Adapter
            browseOder.notifyDataSetChanged();

            Toast.makeText(this, "Đã duyệt hợp đồng " + contractNumber, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Vui lòng chọn một hợp đồng", Toast.LENGTH_SHORT).show();
        }
    }


    private void removeOrderFromPreferences(OrderForBrowse orderToRemove) {
        // Mở SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("OrderData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = sharedPreferences.getString("orders", null); // Lấy danh sách đơn hàng
        Type type = new TypeToken<List<OrderForBrowse>>() {}.getType();

        // Lấy danh sách cũ từ SharedPreferences (nếu có)
        List<OrderForBrowse> orderList = gson.fromJson(json, type);
        if (orderList == null) {
            orderList = new ArrayList<>();
        }

        // Loại bỏ đơn hàng khỏi danh sách
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getSoHopDong().equals(orderToRemove.getSoHopDong())) {
                orderList.remove(i);
                order.remove(orderToRemove); // Cập nhật danh sách hiển thị trong RecyclerView
                break;
            }
        }

        // Chuyển danh sách thành chuỗi JSON và lưu lại
        String updatedJson = gson.toJson(orderList);
        editor.putString("orders", updatedJson);
        editor.apply();
    }



    private void saveOrderToHistory(OrderForBrowse newOrder) {
        // Mở SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("OrderHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = sharedPreferences.getString("orders", null);
        Type type = new TypeToken<List<OrderForHistory>>() {}.getType();

        // Lấy danh sách cũ từ SharedPreferences (nếu có)
        List<OrderForBrowse> orderList = gson.fromJson(json, type);
        if (orderList == null) {
            orderList = new ArrayList<>();
        }

        // Thêm đơn hàng mới vào danh sách
        orderList.add(newOrder);

        // Chuyển danh sách thành chuỗi JSON và lưu lại
        String updatedJson = gson.toJson(orderList);
        editor.putString("orders", updatedJson);
        editor.apply();
    }
    private void loadOrderHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("OrderHistory", MODE_PRIVATE);

        // Lấy chuỗi JSON từ SharedPreferences
        String json = sharedPreferences.getString("orders", null); // Trả về null nếu không có dữ liệu
        if (json != null && !json.isEmpty()) { // Kiểm tra JSON hợp lệ
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<OrderForHistory>>() {}.getType();

            // Chuyển chuỗi JSON thành danh sách đơn hàng
            List<OrderForHistory> loadedOrders = gson.fromJson(json, type);

            if (loadedOrders != null) {
                hisOrder = loadedOrders; // Cập nhật danh sách
            } else {
                hisOrder = new ArrayList<>(); // Tránh lỗi nếu gson trả về null
            }
        } else {
            hisOrder = new ArrayList<>(); // Nếu chưa có dữ liệu, khởi tạo danh sách rỗng
        }
    }

    private void loadOrdersFromPreferences() {
        // Mở SharedPreferences với tên "OrderData"
        SharedPreferences sharedPreferences = getSharedPreferences("OrderData", MODE_PRIVATE);

        // Lấy chuỗi JSON từ SharedPreferences
        String json = sharedPreferences.getString("orders", null); // Trả về null nếu không có dữ liệu

        if (json != null && !json.isEmpty()) { // Kiểm tra JSON hợp lệ
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<OrderForBrowse>>() {}.getType();

            // Chuyển chuỗi JSON thành danh sách đơn hàng
            List<OrderForBrowse> loadedOrders = gson.fromJson(json, type);

            if (loadedOrders != null) {
                order = loadedOrders; // Cập nhật danh sách
            } else {
                order = new ArrayList<>(); // Tránh lỗi nếu gson trả về null
            }
        } else {
            order = new ArrayList<>(); // Nếu chưa có dữ liệu, khởi tạo danh sách rỗng
        }
    }

    private void LoadData(){
        if(!btnMenuOder.isEnabled()){
            loadOrdersFromPreferences();
            browseOder = new BrowseOderAdapter(this, order);
            recyclerView.setAdapter(browseOder);
        }else{
            loadOrderHistory();
            historyAdapter = new HistoryAdapter(this, hisOrder);
            recyclerView.setAdapter(historyAdapter);
        }
    }

    private void OpenMenu(){
        Menu.setVisibility(View.VISIBLE);
        btnMenu.setVisibility(View.GONE);
    }
    private void CloseMenu(){
        Menu.setVisibility(View.GONE);
        btnMenu.setVisibility(View.VISIBLE);
    }
    private void ViewHistory(){
        btnMenu.setVisibility(View.VISIBLE);
        Menu.setVisibility(View.GONE);
        btnMenuOder.setEnabled(true);
        btnMenuHistory.setEnabled(false);
        txtTittle.setText("Lịch Sử");
        btnDuyet.setVisibility(View.GONE);
        LoadData();
    }
    private void ViewOrder(){
        btnMenu.setVisibility(View.VISIBLE);
        Menu.setVisibility(View.GONE);
        btnMenuOder.setEnabled(false);
        btnMenuHistory.setEnabled(true);
        txtTittle.setText("Duyệt Đơn Hàng");
        btnDuyet.setVisibility(View.VISIBLE);
        LoadData();
    }
}