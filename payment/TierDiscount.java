package payment;

/**
 * 티어 할인(데코레이터)
 */
public class TierDiscount extends PaymentDecorator {
    private final double discountRate;

    public TierDiscount(Payment decoratedPayment, double discountRate) {
        super(decoratedPayment);
        this.discountRate = discountRate;
    }

    @Override
    public double pay() {
        double original = decoratedPayment.pay();
        double tier_discount = original * (1.0 - discountRate);
        System.out.println("티어할인 적용 (" + (discountRate * 100) + "% 할인): " + tier_discount + "원");
        return tier_discount;
    }
}