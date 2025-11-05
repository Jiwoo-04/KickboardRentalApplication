package payment;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentHistoryDB {

    private static final String FILE_NAME = "payment_history.csv";

    public static void savePaymentRecord(String userName, String vehicleType,
                                         int minutes, double totalFee,
                                         boolean couponUsed) {
        try {
            boolean isNewFile = !(new File(FILE_NAME).exists());

            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(FILE_NAME, true), "UTF-8"))) {

                // 새 파일이면 BOM 추가 → Excel에서 UTF-8 한글 깨짐 방지
                if (isNewFile) {
                    writer.write('\uFEFF'); // BOM
                    writer.write("사용자,이용수단,이용시간(분),결제금액(원),쿠폰사용,결제시간");
                    writer.newLine();
                }

                String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String record = String.format("%s,%s,%d,%.2f,%s,%s\n",
                        userName, vehicleType, minutes, totalFee,
                        couponUsed ? "YES" : "NO", time);

                writer.write(record);
            }

            System.out.println("[DB] 결제 내역이 CSV 파일에 저장되었습니다.");

        } catch (IOException e) {
            System.err.println("[DB 오류] 결제 내역 저장 중 문제 발생: " + e.getMessage());
        }
    }
}
