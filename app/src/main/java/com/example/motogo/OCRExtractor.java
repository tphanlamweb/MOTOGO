package com.example.motogo;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OCRExtractor {

    private static final String TAG = "OCRExtractor"; // Tag dùng để debug log
    private final OCRCallback callback; // Biến callback để gửi kết quả về CameraActivity
    private Pattern dobPattern = Pattern.compile("\\b(\\d{2}/\\d{2}/\\d{4})\\b"); // Ngày sinh: dd/mm/yyyy
    private Pattern genderPattern = Pattern.compile("\\b(Nam|Nữ)\\b", Pattern.CASE_INSENSITIVE); // Giới tính

    // Constructor, nhận vào một callback từ CameraActivity
    public OCRExtractor(OCRCallback callback) {
        this.callback = callback;
    }

    /**
     * Phương thức này thực hiện nhận diện văn bản từ ảnh.
     * @param bitmap Ảnh đầu vào (chụp từ Camera)
     */
    public void extractText(Bitmap bitmap) {
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS); // Khởi tạo OCR
        InputImage image = InputImage.fromBitmap(bitmap, 0); // Chuyển ảnh thành định dạng InputImage

        // Thực hiện xử lý nhận diện văn bản
        recognizer.process(image)
                .addOnSuccessListener(text -> {
                    String resultText = text.getText(); // Lấy kết quả OCR
                    Log.d(TAG, "Extracted Text: " + resultText);

                    // Gọi phương thức parseText để trích xuất dữ liệu cụ thể
                    Map<String, String> extractedData = parseText(resultText);

                    // Gửi kết quả về CameraActivity thông qua callback
                    callback.onOCRResult(extractedData);
                })
                .addOnFailureListener(e -> Log.e(TAG, "Text recognition failed: " + e.getMessage())); // Xử lý lỗi nếu có
    }

    /**
     * Phương thức này thực hiện trích xuất thông tin từ chuỗi văn bản đã nhận diện.
     * @param text Văn bản đầu vào từ OCR
     * @return Map<String, String> chứa dữ liệu đã trích xuất
     */



    private Map<String, String> parseText(String text) {
        Map<String, String> extractedData = new LinkedHashMap<>();

        // Trích xuất các thông tin theo từng biểu thức chính quy
        extractedData.put("cccd", extractField(text, "\\b\\d{12}\\b")); // Số CCCD

        // Trích xuất họ và tên (dòng ngay sau "Họ và tên / Full name:")
        extractedData.put("name", extractNextLine(text, "name:"));

        extractedData.put("dob", extractField(text, "\\b\\d{2}/\\d{2}/\\d{4}\\b")); // Ngày sinh (dd/MM/yyyy)

        // Trích xuất giới tính (Nam hoặc Nữ)
        extractedData.put("gender", extractField(text, "(?<=Sex: )Nam|Nữ"));

        extractedData.put("nationality", extractField(text, "(?<=Nationality:?)\\s*([\\p{L} ]+)"));

        // Trích xuất quê quán (theo định dạng Phường, Quận, Thành phố)
        extractedData.put("hometown", extractNextLine(text,"(?<=origin:?)"));

        // Trích xuất nơi thường trú (nằm sau "Nơi thường trú/Place of residence:")
        extractedData.put("address", extractField(text, "(?<=residence:?)\\s*([\\p{L}0-9,\\s.-]+)"));


        // Đảm bảo không có giá trị null
        extractedData.replaceAll((key, value) -> value != null ? value.trim() : "");

        return extractedData;
    }

    /**
     * Phương thức này sử dụng regex để tìm kiếm một trường dữ liệu trong văn bản.
     * @param text Chuỗi văn bản từ OCR
     * @param regex Biểu thức chính quy dùng để tìm kiếm
     * @return Chuỗi chứa giá trị của trường dữ liệu hoặc "Không có dữ liệu" nếu không tìm thấy
     */
    private String extractField(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group().trim() : "Không có dữ liệu";
    }

    // Interface callback để gửi kết quả về CameraActivity
    public interface OCRCallback {
        void onOCRResult(Map<String, String> extractedData);
    }

    private String extractNextLine(String text, String headerPattern) {
        String[] lines = text.split("\\r?\\n"); // Chia thành từng dòng
        Pattern pattern = Pattern.compile(headerPattern, Pattern.CASE_INSENSITIVE);

        for (int i = 0; i < lines.length - 1; i++) {
            if (pattern.matcher(lines[i].trim()).find()) {
                return lines[i + 1].replaceAll("[^\\p{L} ]", "").trim(); // Chỉ lấy ký tự chữ, loại bỏ số và ký tự lạ
            }
        }
        return "Không có dữ liệu"; // Nếu không tìm thấy
    }
}