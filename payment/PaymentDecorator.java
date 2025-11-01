//데코레이터 추상 클래스
package payment;

public abstract class PaymentDecorator implements Payment {
    protected Payment decoratedPayment;

    public PaymentDecorator(Payment decoratedPayment) {
        this.decoratedPayment = decoratedPayment;
    }

    @Override
    public double pay() {
        return decoratedPayment.pay();
    }
}
