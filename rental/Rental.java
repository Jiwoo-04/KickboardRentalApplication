package rental;

import payment.*;   // payment 폴더 내 클래스들 import
import vehicle.*;   // vehicle 폴더 내 Vehicle, Bicycle, Kickboard 등 import

public class Rental {
    private Vehicle vehicle;   // 대여한 탈것 (자전거, 킥보드 등)
    private int minutes;       // 대여 시간 (분)

    public Rental(Vehicle vehicle, int minutes) {
        this.vehicle = vehicle;
        this.minutes = minutes;
    }

    /**
     * 결제 처리 메서드
     * @param useCoupon 쿠폰 사용 여부
     * @param userName 사용자 이름
     * @return 최종 결제 금액
     */

    public double processPayment(boolean useCoupon, String userName) {
        // 1️⃣ 기본 요금
        Payment payment = new BasicPayment(vehicle.getBasefee());

        // 2️⃣ 시간 가산 요금 추가 (Decorator)
        payment = new TimeBasedFee(payment, vehicle.getRatePerMinute(), minutes);

        // 3️⃣ 쿠폰 적용 (선택적 Decorator)
        if (useCoupon) {
            payment = new CouponDiscount(payment, 0.2); // 20% 할인
        }

        // 4️⃣ 결제 금액 계산
        double total = payment.pay();

        // 5️⃣ 결제 내역을 CSV 파일에 저장
        PaymentHistoryDB.savePaymentRecord(
                userName,                // 사용자 이름
                vehicle.getId(),       // 차량 이름 (예: "Bicycle")
                minutes,                 // 이용 시간
                total,                   // 최종 결제 금액
                useCoupon                // 쿠폰 사용 여부
        );

        return total;
    }
}
