//쿠폰 할인
package payment;

public class CouponDiscount extends PaymentDecorator {
    private double discountRate;

    public CouponDiscount(Payment decoratedPayment, double discountRate) {
        super(decoratedPayment);
        this.discountRate = discountRate;
    }

    @Override
    public double pay() {
        double amount = decoratedPayment.pay();
        double discounted = amount * (1 - discountRate);
        System.out.println("쿠폰 적용 (" + (discountRate * 100) + "% 할인): " + discounted + "원");
        return discounted;
    }
}