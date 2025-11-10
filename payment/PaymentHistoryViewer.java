package payment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 결제 내역을 CSV에서 조회하는 클래스
 */
public class PaymentHistoryViewer {

    private static final String FILE_NAME = "payment_history.csv";

    /**
     * 특정 사용자 ID의 결제 내역 중 최근 5개만 출력
     * @param userId 로그인된 사용자 ID
     */
    public static void viewPaymentHistory(String userId) {
        System.out.println("\n==== [" + userId + "님의 최근 결제 내역 5건] ====");

        List<String[]> userPayments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(FILE_NAME, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // ✅ 헤더나 공백, null이 포함된 줄은 건너뛴다
                if (line.startsWith("UserID") || line.trim().isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 7 && data[0].equals(userId)) {
                    userPayments.add(data);
                }
            }

            if (userPayments.isEmpty()) {
                System.out.println("결제 내역이 없습니다.");
                return;
            }

            // ✅ 최근 5건만 표시 (파일이 오래된 순서로 저장되었다고 가정)
            int start = Math.max(0, userPayments.size() - 5);
            List<String[]> recentPayments = userPayments.subList(start, userPayments.size());

            System.out.printf("%-10s %-10s %-12s %-8s %-10s %-8s %-20s\n",
                    "UserID", "Name", "Vehicle", "Time", "Fee", "Coupon", "Date");
            System.out.println("-----------------------------------------------------------------------");

            for (String[] data : recentPayments) {
                System.out.printf("%-10s %-10s %-12s %-8s %-10s %-8s %-20s\n",
                        data[0], data[1], data[2], data[3], data[4], data[5], data[6]);
            }

        } catch (IOException e) {
            System.out.println("[조회 오류] 결제 내역을 불러오는 중 문제가 발생했습니다: " + e.getMessage());
        }
    }
}