//Test용 파일
package payment;
import java.util.Scanner;

public class TestPay {
    public static void main(String[] args) {
        double baseFee = 3000;
        int minutes = 15;

        Payment payment = new BasicPayment(baseFee);
        payment = new TimeBasedFee(payment, 100, minutes);
        payment = new CouponDiscount(payment, 0.1); // 10% 할인

        System.out.println("\n--- 결제 내역 ---");
        double total = payment.pay();
        System.out.println("최종 결제 금액: " + total + "원");
    }
}