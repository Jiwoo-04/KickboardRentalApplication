package rental;

import payment.*;
import vehicle.Vehicle;

public class Rental {
    private Vehicle vehicle;   // 대여한 탈것
    private int minutes;       // 대여 시간 (분)

    public Rental(Vehicle vehicle, int minutes) {
        this.vehicle = vehicle;
        this.minutes = minutes;
    }

    /**
     * 결제 처리 메서드
     * @param useCoupon 쿠폰 사용 여부
     * @param userID 로그인한 사용자 ID
     * @param userName 사용자 이름
     * @return 최종 결제 금액
     */
    public double processPayment(boolean useCoupon, String userID, String userName) {
        // 기본 요금
        Payment payment = new BasicPayment(vehicle.getBasefee());

        // 시간 가산 요금 추가
        payment = new TimeBasedFee(payment, vehicle.getRatePerMinute(), minutes);

        // 쿠폰 적용
        if (useCoupon) {
            payment = new CouponDiscount(payment, 0.2);
        }

        double total = payment.pay();

        // DB에 결제 내역 저장
        PaymentHistoryDB.savePaymentRecord(
                userID,
                userName,
                vehicle.getId(),
                minutes,
                total,
                useCoupon
        );

        return total;
    }
}

