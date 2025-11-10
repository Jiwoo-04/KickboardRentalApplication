package payment;

import vehicle.Bike;
import rental.Rental;

public class TestPay {
    public static void main(String[] args) {
        // 1️⃣ 자전거 객체 생성 (기본요금 1000원, 분당요금 50원)
        Bike bike = new Bike("Bike-01");

        // 2️⃣ Rental 생성 (대여 시간 60분)
        Rental rental = new Rental(bike, 60);

        // 3️⃣ 결제 처리 (쿠폰 사용함, ID "user001", 이름 "심지승")
        double totalFee = rental.processPayment(true, "user001", "심지승");

        // 4️⃣ 결과 출력
        System.out.println("\n===== 결제 테스트 결과 =====");
        System.out.println("사용자 ID: user001");
        System.out.println("사용자 이름: 심지승");
        System.out.println("이용수단: " + bike.getType());
        System.out.println("이용시간: 60분");
        System.out.printf("최종 결제 금액: %.2f원\n", totalFee);
        System.out.println("===========================");
    }
}