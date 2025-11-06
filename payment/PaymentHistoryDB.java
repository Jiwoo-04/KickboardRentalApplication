package payment;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 결제 내역을 CSV 파일에 저장하는 클래스 (UTF-8 인코딩 적용)
 */
public class PaymentHistoryDB {

    private static final String FILE_NAME = "payment_history.csv";

    public static void savePaymentRecord(String userID, String userName, String vehicleType,
                                         int minutes, double totalFee, boolean couponUsed) {
        boolean fileExists = new File(FILE_NAME).exists();

        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(FILE_NAME, true), StandardCharsets.UTF_8)) {

            // 파일이 처음 만들어질 때 헤더 추가
            if (!fileExists) {
                writer.write("UserID,UserName,VehicleType,Minutes,TotalFee,CouponUsed,DateTime\n");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String time = LocalDateTime.now().format(formatter);

            // CSV 한 줄 형식: ID, 이름, 수단, 이용시간, 결제금액, 쿠폰사용, 시간
            String record = String.format("%s,%s,%s,%d,%.2f,%s,%s\n",
                    userID, userName, vehicleType, minutes, totalFee,
                    couponUsed ? "YES" : "NO", time);

            writer.write(record);
            writer.flush();

            System.out.println("[DB] 결제 내역이 CSV 파일에 저장되었습니다.");

        } catch (IOException e) {
            System.err.println("[DB 오류] 결제 내역 저장 중 문제 발생: " + e.getMessage());
        }
    }
}
