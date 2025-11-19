package payment;

/**
 * 티어 할인(데코레이터)
 */
public class TierDiscount implements Payment {
    private final Payment payment;
    private final double discountRate;

    public TierDiscount(Payment payment, double discountRate) {
        this.payment = payment;
        this.discountRate = discountRate;
    }

    @Override
    public double pay() {
        double original = payment.pay(); // 정상 작동
        double tier_discount = original * (1.0 - discountRate);
        System.out.println("티어할인 적용 (" + (discountRate * 100) + "% 할인): " + tier_discount + "원");
        return tier_discount;
    }
}