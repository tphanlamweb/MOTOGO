package com.example.motogo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {
    private ImageView imgProduct;
    private TextView txtProductName, txtTotalPrice, txtNgayThue, txtNgayTra, txtGiaNgay, txtCCCD, txtHoTen, txtSoDT, txtViTri;
    private Button btnStartDate, btnEndDate, ScanID, btnRent;
    private ImageButton imgCoc;
    private int pricePerDay;  // Giá thuê mỗi ngày
    private int year, month, day;
    private static List<OrderForBrowse> oderForBrowse = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1;
    private BrowseOderAdapter browse;
    private Uri imageUri;
    private Bitmap selectedBitmap;
    private ImageView imgPreview; // Thêm ImageView để hiển thị ảnh chọn
    private ActivityResultLauncher<Intent> scanCccdLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Ánh xạ các View
        CreateView();
        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        int imageRes = intent.getIntExtra("Image", 0);
        pricePerDay = intent.getIntExtra("Price", 0);  // Nhận giá thuê từ ProductAdapter

        // Hiển thị dữ liệu lên giao diện
        imgProduct.setImageResource(imageRes);
        txtProductName.setText(name);
        ShowPrice(pricePerDay, txtGiaNgay);

        // Thiết lập chọn ngày Trả xe
        btnStartDate.setOnClickListener(v -> SetHireDate());

        // Thiết lập chọn ngày Thuê xe
        btnEndDate.setOnClickListener(v -> SetReturnDay());

        GetData();
        // Button Scan CCCD
        ScanID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingActivity.this, CameraActivity.class);
                scanCccdLauncher.launch(intent);
            }
        });
        // Button Thue
        btnRent.setOnClickListener(v -> ButtonRent() );

        imgCoc.setOnClickListener(v -> openGallery());
        CreateFakeDataForTest3();
    }
    private void  CreateFakeDataForTest(){
        txtNgayThue.setText("01/01/2025");
        txtNgayTra.setText("02/01/2025");
        txtHoTen.setText("ThanhPhan");
        txtCCCD.setText("048204001942");;
        txtSoDT.setText("0905678909");
        txtViTri.setText("27 hoang dieu");
        calculateTotalPrice();
    }


    private void  CreateFakeDataForTest2(){
        txtNgayThue.setText("03/01/2025");
        txtNgayTra.setText("06/01/2025");
        txtHoTen.setText("phan");
        txtCCCD.setText("12545678");;
        txtSoDT.setText("0905678909");
        txtViTri.setText("99999999");
        calculateTotalPrice();
    }

    private void  CreateFakeDataForTest3(){
        txtNgayThue.setText("21/11/2025");
        txtNgayTra.setText("23/11/2025");
        txtHoTen.setText("ThanhPhan");
        txtCCCD.setText("7777777");;
        txtSoDT.setText("0905678909");
        txtViTri.setText("000000000");
        calculateTotalPrice();
    }
    private void CreateOrder(){
        // Giả sử bạn có các dữ liệu cần thiết
        String soCCCD = txtCCCD.getText().toString();
        String soHopDong = ContractNumberGenerator.CreateNewContractNumber(); // Hàm tạo số hợp đồng ngẫu nhiên
        int tongGiaTien = ChangePrice(txtTotalPrice.getText().toString()) ; // Hàm tính tổng tiền thuê
        String tenKH = txtHoTen.getText().toString();
        String ngayThue = txtNgayThue.getText().toString();
        String ngayTra = txtNgayTra.getText().toString();
        String SoDT = txtSoDT.getText().toString();
        String ViTri = txtViTri.getText().toString();
        Bitmap AnhCK = selectedBitmap;

        // Lưu vào danh sách RecentOderList
        oderForBrowse.add(new OrderForBrowse(soCCCD, soHopDong, tongGiaTien, tenKH, ngayTra, ngayThue, SoDT, ViTri , AnhCK)) ;

        saveOrderToPreferences(new OrderForBrowse(soCCCD, soHopDong, tongGiaTien, tenKH, ngayTra, ngayThue, SoDT, ViTri , AnhCK));

        // Hiển thị thông báo
        Toast.makeText(BookingActivity.this, "Bạn đã đặt xe thành công. Hãy đợi quản lý liên hệ với bạn nhé.", Toast.LENGTH_LONG).show();

        // Quay về MainActivity
        Intent intent = new Intent(BookingActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
 // Kết thúc BookingActivity
    }

    private void saveOrderToPreferences(OrderForBrowse newOrder) {
        // Mở SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("OrderData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = sharedPreferences.getString("orders", null);
        Type type = new TypeToken<List<OrderForBrowse>>() {}.getType();

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

    // Hàm để lấy danh sách đơn hàng từ bất kỳ đâu
    public static List<OrderForBrowse> getRecentOrderList() {
        return oderForBrowse;
    }

    private void ButtonRent(){
        if(CheckNullData() == 1){
            return;
        }else {
            CreateOrder();
        }
    }

    private int CheckNullData(){
        if (txtTotalPrice.getText().toString().equals("0 VND") || txtTotalPrice.getText().toString().equals("Vui lòng nhập ngày hợp lệ!") || txtTotalPrice.getText().toString().equals("Ngày trả phải sau ngày thuê!")){
            Toast.makeText(this, "Bạn chưa chọn ngày hợp lệ", Toast.LENGTH_SHORT).show();
            return 1;
        }
        if (txtHoTen.getText().toString().equals("Hãy quét căn cước của bạn để lấy dữ liệu") || txtHoTen.getText().toString().isEmpty() || txtHoTen.getText().toString().equals("Không có dữ liệu") || txtCCCD.getText().toString().equals("Không có dữ liệu") || txtCCCD.getText().toString().equals("Hãy quét căn cước của bạn để lấy dữ liệu") || txtCCCD.getText().toString().isEmpty()){
            Toast.makeText(this, "Hãy quét căn cước của bạn để lấy dữ liệu tên và số căn cước", Toast.LENGTH_SHORT).show();
            return 1;
        }
        if (txtSoDT.getText().toString().isEmpty()){
            Toast.makeText(this, "Bạn chưa nhập số điện thoại ", Toast.LENGTH_SHORT).show();
            return 1;
        }
        if (txtViTri.getText().toString().isEmpty()){
            Toast.makeText(this, "Bạn chưa nhập địa chỉ nhận xe", Toast.LENGTH_SHORT).show();
            return 1;
        }
        return CheckDate();

    }

    private int CheckDate(){
        // Kiểm tra ngày thuê có hợp lệ không
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date selectedDate = sdf.parse(txtNgayThue.getText().toString());
            Date today = new Date(); // Lấy ngày hiện tại

            // Nếu ngày thuê nhỏ hơn hôm nay => Thông báo lỗi
            if (selectedDate.before(today)) {
                Toast.makeText(this, "Vui lòng chọn ngày thuê hợp lệ!.", Toast.LENGTH_SHORT).show();
                calculateTotalPrice();
                return 1;
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Lỗi định dạng ngày", Toast.LENGTH_SHORT).show();
            return 1;
        }
        return 0;
    }


    private void calculateTotalPrice() {
        if (txtNgayTra.getText().toString().isEmpty()) {
            Toast.makeText(this, "Bạn chưa nhập ngày trả xe", Toast.LENGTH_SHORT).show();
            return;
        } else if (txtNgayThue.getText().toString().isEmpty()) {
            Toast.makeText(this, "Bạn chưa nhập ngày thuê xe", Toast.LENGTH_SHORT).show();
            return;
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate startDate = LocalDate.parse(txtNgayThue.getText().toString(), formatter);
                LocalDate endDate = LocalDate.parse(txtNgayTra.getText().toString(), formatter);

                long days = ChronoUnit.DAYS.between(startDate, endDate);

                if (days > 0) {
                    int totalPrice = (int) (days * pricePerDay);
                    ShowPrice(totalPrice, txtTotalPrice);
                } else if (days == 0) {
                    ShowPrice(pricePerDay, txtTotalPrice);
                } else {
                    txtTotalPrice.setText("Ngày trả phải sau ngày thuê!");
                }
            } catch (Exception e) {
                txtTotalPrice.setText("Vui lòng nhập ngày hợp lệ!");
            }
        }
    }
    public int ChangePrice(String price) {
        // Loại bỏ ký tự không phải số (dấu chấm, chữ vnđ, khoảng trắng)
        String cleanPrice = price.replaceAll("[^0-9]", "");

        // Chuyển về số nguyên
        return Integer.parseInt(cleanPrice);
    }

    private void SetHireDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                BookingActivity.this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    txtNgayThue.setText(selectedDate);
                    calculateTotalPrice();

                },
                year, month, day);
        datePickerDialog.show();
    }

    private void SetReturnDay() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                BookingActivity.this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    txtNgayTra.setText(selectedDate);
                    calculateTotalPrice();

                },
                year, month, day);
        datePickerDialog.show();
    }

    private void CreateView() {
        imgProduct = findViewById(R.id.imgMoto);
        txtProductName = findViewById(R.id.txtMotoName);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);
        txtNgayThue = findViewById(R.id.txtNgayThue);
        txtNgayTra = findViewById(R.id.txtNgayTra);
        txtGiaNgay = findViewById(R.id.txtGiaNgay);
        ScanID = findViewById(R.id.btnScanID);
        txtCCCD = findViewById(R.id.txtCCCD);
        txtHoTen = findViewById(R.id.txtHoTen);

        btnRent = findViewById(R.id.btnRent);

        txtSoDT = findViewById(R.id.txtSoDT);

        // Tạo giá trị đặt vị trí
        txtViTri = findViewById(R.id.txtLocation);
        imgCoc = findViewById(R.id.imgCoc);
    }


    private void ShowPrice(int Gia, TextView textView) {
        int GiaSP = Gia; // Giá sản phẩm

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(GiaSP) + " vnđ";

        textView.setText(formattedPrice);
    }


    private void GetData(){
        scanCccdLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String soCCCD = data.getStringExtra("SO_CCCD");
                        String hoTen = data.getStringExtra("HO_TEN");
                        String ngaySinh = data.getStringExtra("NGAY_SINH");
                        String gioiTinh = data.getStringExtra("GIOI_TINH");
                        String quocTich = data.getStringExtra("QUOC_TICH");
                        String queQuan = data.getStringExtra("QUE_QUAN");
                        String noiThuongTru = data.getStringExtra("NOI_THUONG_TRU");

                        // Cập nhật UI
                        txtCCCD.setText(soCCCD);
                        txtHoTen.setText(hoTen);
                    }
                }
        );
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    // Xử lý kết quả khi chọn ảnh
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            try {
                                // Đọc ảnh với chất lượng cao nhất
                                selectedBitmap = loadBitmap(imageUri);
                                imgCoc.setImageBitmap(selectedBitmap);
                                saveBitmapToPNG(selectedBitmap, "payment_proof.png");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
    );

    // Hàm đọc ảnh từ Uri thành Bitmap (ARGB_8888)
    private Bitmap loadBitmap(Uri uri) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(getContentResolver(), uri),
                    (decoder, info, source) -> decoder.setAllocator(ImageDecoder.ALLOCATOR_SOFTWARE)
            );
        } else {
            return MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        }
    }

    // Hàm lưu ảnh dưới dạng PNG với chất lượng cao nhất
    private void saveBitmapToPNG(Bitmap bitmap, String fileName) {
        try (FileOutputStream out = openFileOutput(fileName, MODE_PRIVATE)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
