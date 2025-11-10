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

    public static void savePaymentRecord(String userID, String userName,
                                         String vehicleID, int minutes,
                                         double totalFee, boolean useCoupon) {

        // ✅ userID나 userName이 null이면 저장하지 않음
        if (userID == null || userName == null) {
            System.out.println("[경고] 사용자 정보가 비어있어 결제 내역을 저장하지 않습니다.");
            return;
        }

        File file = new File(FILE_NAME);
        boolean fileExists = file.exists();

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {

            // ✅ 파일이 새로 만들어질 때만 헤더 추가
            if (!fileExists) {
                writer.write("UserID,UserName,VehicleType,Minutes,TotalFee,CouponUsed,DateTime\n");
            }

            String couponText = useCoupon ? "YES" : "NO";
            String now = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // ✅ 실제 데이터만 기록
            writer.write(String.format("%s,%s,%s,%d,%.2f,%s,%s%n",
                    userID, userName, vehicleID, minutes, totalFee, couponText, now));

        } catch (IOException e) {
            System.err.println("[저장 오류] 결제 내역 저장 실패: " + e.getMessage());
        }
    }
}