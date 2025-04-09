package com.example.motogo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Check_Information_Exctracted extends AppCompatActivity {

    private TextView cccd, ten, dob, gender, national, home, address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_information_exctracted);

        // Ánh xạ TextView từ layout
        cccd = findViewById(R.id.txtSoCCCD);
        ten = findViewById(R.id.txtTen);
        dob = findViewById(R.id.txtNgaySinh);
        gender = findViewById(R.id.txtGioiTinh);
        national = findViewById(R.id.txtQuocTich);
        home = findViewById(R.id.txtQueQuan);
        address = findViewById(R.id.txtThuongTru);

        // Nhận dữ liệu từ Intent và hiển thị lên giao diện
        displayExtractedInfo();
    }

    private void displayExtractedInfo() {
        // Nhận dữ liệu từ Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cccd.setText(extras.getString("cccd")); ;
            ten.setText(extras.getString("name"));
            dob.setText(extras.getString("dob"));
            gender.setText(extras.getString("gender"));
            national.setText(extras.getString("nationality"));
            home.setText(extras.getString("hometown"));
            address.setText(extras.getString("address"));
        }
    }
}