package rental;

import AccountManage.accountmanage.Account;
import payment.*;
import vehicle.Vehicle;
import AccountManage.accountmanage.tier.Tier;

import java.sql.SQLException;


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
    public double processPayment(boolean useCoupon, String userID, String userName, Account account) throws SQLException {
        // 기본 요금
        Payment payment = new BasicPayment(vehicle.getBasefee());

        // 시간 가산 요금 추가
        payment = new TimeBasedFee(payment, vehicle.getRatePerMinute(), minutes);

        // 2) 티어 할인 적용 (Tier 객체의 할인율 사용)
        Tier tier = account.tier(); // Account에서 현재 티어 반환
        double tierDiscount = 0.0;
        if (tier != null) {
            // Tier 클래스는 getDiscountRate() 를 제공한다고 가정 (예: 0.2, 0.1, 0.0)
            tierDiscount = tier.getDiscountRate();
            if (tierDiscount > 0) {
                payment = new TierDiscount(payment, tierDiscount);
            }
        }

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

