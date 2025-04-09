package com.example.motogo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ContractNumberGenerator {
    private static final Set<String> generatedContractNumbers = new HashSet<>();
    private static final Random random = new Random();

    public static String CreateNewContractNumber() {
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String contractNumber;

        do {
            int randomPart = 1000 + random.nextInt(9000); // Tạo số ngẫu nhiên 4 chữ số (1000-9999)
            contractNumber = "HD-" + datePart + "-" + randomPart;
        } while (generatedContractNumbers.contains(contractNumber)); // Đảm bảo không trùng lặp

        generatedContractNumbers.add(contractNumber);
        return contractNumber;
    }
}