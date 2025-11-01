//기본 결제 대여료
package payment;

public class BasicPayment implements Payment {
    private double baseFee;

    public BasicPayment(double baseFee) {
        this.baseFee = baseFee;
    }

    @Override
    public double pay() {
        System.out.println("기본 대여료: " + baseFee + "원");
        return baseFee;
    }
}