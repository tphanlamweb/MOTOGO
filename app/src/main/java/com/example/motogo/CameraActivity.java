package com.example.motogo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CameraActivity extends AppCompatActivity implements OCRExtractor.OCRCallback {

    private OCRExtractor ocrExtractor; // Đối tượng OCRExtractor để nhận diện văn bản
    private ImageView imageView; // Hiển thị ảnh chụp
    private TextView txtWN;
    private Uri photoURI = null; // URI ảnh chụp
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    imageView.setImageURI(photoURI); // Hiển thị ảnh lên ImageView
                    Bitmap imageBitmap;
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);
                        ocrExtractor.extractText(imageBitmap); // Gửi ảnh đến OCRExtractor để xử lý
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi khi xử lý ảnh!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Chụp ảnh thất bại!", Toast.LENGTH_SHORT).show();
                    photoURI = null;
                }
                if (photoURI != null)txtWN.setVisibility(View.VISIBLE);
            }
    );

    // Biến lưu trữ thông tin trích xuất từ căn cước công dân
    public String cccd, ten, dob, gender, national, home, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.imageView); // Liên kết ImageView với UI
        ocrExtractor = new OCRExtractor(this); // Khởi tạo OCRExtractor với callback



        Button btnCapture = findViewById(R.id.btnCapture);
        txtWN = findViewById(R.id.txtWarning);
        btnCapture.setOnClickListener(v -> captureImage()); // Khi nhấn nút, mở camera để chụp ảnh

        //Nut Tra Ve
        Button Check = findViewById(R.id.btnCheckInfo);
        Check.setOnClickListener(v -> ShowDemoData());

        Button btnOK = findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> returnCccdData(cccd,ten,dob,gender,national,home,address));

    }

    /**
     * Nhận kết quả từ OCRExtractor và cập nhật giao diện.
     * @param extractedData Dữ liệu trích xuất từ căn cước công dân
     */
    @Override
    public void onOCRResult(Map<String, String> extractedData) {
        // Gán giá trị trích xuất từ OCR vào các biến
        this.cccd = extractedData.get("cccd");
        this.ten = extractedData.get("name");
        this.dob = extractedData.get("dob");
        this.gender = extractedData.get("gender");
        this.national = extractedData.get("nationality");
        this.home = extractedData.get("hometown");
        this.address = extractedData.get("address");
        ShowDemoData();
    }
    private void captureImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = new File(getExternalFilesDir("images"), "cccd_photo.jpg"); // Thư mục hợp lệ
        photoURI = FileProvider.getUriForFile(this, "com.example.motogo.fileprovider", photoFile);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION); // Cấp quyền ghi

        cameraLauncher.launch(takePictureIntent);
    }

    private void returnCccdData(String soCCCD, String hoTen, String ngaySinh,
                                String gioiTinh, String quocTich, String queQuan, String noiThuongTru) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("SO_CCCD", soCCCD);
        resultIntent.putExtra("HO_TEN", hoTen);
        resultIntent.putExtra("NGAY_SINH", ngaySinh);
        resultIntent.putExtra("GIOI_TINH", gioiTinh);
        resultIntent.putExtra("QUOC_TICH", quocTich);
        resultIntent.putExtra("QUE_QUAN", queQuan);
        resultIntent.putExtra("NOI_THUONG_TRU", noiThuongTru);

        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
    private void ShowDemoData(){
        Toast.makeText(CameraActivity.this, "THÔNG TIN ĐÃ TRÍCH XUẤT:" + "\nCCCD: " + cccd + "\nTên: " + ten, Toast.LENGTH_LONG).show();

/*        // Hiển thị thông tin đầy đủ trên giao diện người dùng
        Toast.makeText(CameraActivity.this, "THÔNG TIN ĐÃ TRÍCH XUẤT:" +
                        "\nCCCD: " + cccd +
                        "\nTên: " + ten +
                        "\ndob: " + dob +
                        "\ngender: " + gender +
                        "\nnation: " + national +
                        "\nhome: " + home +
                        "\nadd: " + address
                , Toast.LENGTH_LONG).show();
 */
    }
}
