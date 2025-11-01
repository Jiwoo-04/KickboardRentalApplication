//시간에 따른 추가 요금
package payment;

public class TimeBasedFee extends PaymentDecorator {
    private double ratePerMinute;
    private int minutes;

    public TimeBasedFee(Payment decoratedPayment, double ratePerMinute, int minutes) {
        super(decoratedPayment);
        this.ratePerMinute = ratePerMinute;
        this.minutes = minutes;
    }

    @Override
    public double pay() {
        double base = decoratedPayment.pay();
        double extra = ratePerMinute * minutes;
        double total = base + extra;
        System.out.println("대여 시간: " + minutes + "분, 시간 가산요금: " + extra + "원");
        System.out.println("총 결제 금액: " + total + "원");
        return total;
    }
}