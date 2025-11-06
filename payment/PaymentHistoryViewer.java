package payment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 결제 내역을 CSV에서 조회하는 클래스
 */
public class PaymentHistoryViewer {

    private static final String FILE_NAME = "payment_history.csv";

    /**
     * 🔹 전체 결제 내역을 출력 (관리자용 or 테스트용)
     */
    public static void viewPaymentHistory() {
        System.out.println("\n==== [전체 결제 내역] ====");
        try (BufferedReader reader = new BufferedReader(
                new FileReader(FILE_NAME, StandardCharsets.UTF_8))) {

            String line;
            System.out.printf("%-10s %-8s %-12s %-8s %-10s %-8s %-20s\n",
                    "UserID", "Name", "Vehicle", "Time", "Fee", "Coupon", "Date");

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7) {
                    System.out.printf("%-10s %-8s %-12s %-8s %-10s %-8s %-20s\n",
                            data[0], data[1], data[2], data[3], data[4], data[5], data[6]);
                }
            }

        } catch (IOException e) {
            System.out.println("[조회 오류] 결제 내역을 불러오는 중 문제가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 🔹 로그인한 사용자 ID 기준으로 최근 N건의 결제 내역만 출력
     */
    public static void showRecentPayments(String userID, int limit) {
        List<String> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(FILE_NAME, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(userID + ",")) {
                    records.add(line);
                }
            }

            int start = Math.max(0, records.size() - limit);
            System.out.println("\n📘 [" + userID + "]님의 최근 결제 내역 (" + (records.size() - start) + "건):");
            for (int i = start; i < records.size(); i++) {
                String[] data = records.get(i).split(",");
                if (data.length >= 7) {
                    System.out.printf(" - 수단: %s | 시간: %s분 | 결제금액: %s원 | 쿠폰: %s | 일시: %s%n",
                            data[2], data[3], data[4], data[5], data[6]);
                }
            }

        } catch (IOException e) {
            System.err.println("[DB 오류] 결제 내역을 불러올 수 없습니다: " + e.getMessage());
        }
    }
}
