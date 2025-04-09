package com.example.motogo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private int clickCount = 0;
    private Handler handler = new Handler();

    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    CardView AdBtn ;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        // Tạo danh sách sản phẩm mẫu
        CreateList();

        String anh = "res/drawable/avatar.jpg";
        AdBtn=findViewById(R.id.ADBtn);
        // Gán adapter cho RecyclerView
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);
        AdBtn.setOnClickListener(v -> setupButton());

    }
    private void setupButton() {
            clickCount++;
            if (clickCount == 3) {
                clickCount = 0; // Reset đếm
                AdLogin();
            }
            // Đặt lại biến đếm sau 1 giây nếu không nhấn tiếp
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(() -> clickCount = 0, 1000);
    }
    private void CreateList(){
        productList.add(new Product(R.drawable.winnerx,"Xe Winner X", "", 150000, "43A-01234","0"));
        productList.add(new Product(R.drawable.sh,"Xe SH", "", 250000, "43D1-54321", "0"));
        productList.add(new Product(R.drawable.wave,"Xe Wave", "", 100000, "43D2-98765", "0"));
        productList.add(new Product(R.drawable.vision , "Xe Vision" ,"" , 200000, "43C1-56789", "0"));
        productList.add(new Product(R.drawable.shmode , "Xe SH mode" ,"" , 120000, "43C2-44444", "0"));
        productList.add(new Product(R.drawable.cbr, "Xe CBR 150" ,"" , 300000, "43A1-22333", "0"));
    }

    private void AdLogin(){
        Intent intent = new Intent(this, LoginForAdmin.class);
        startActivity(intent);
    }
}