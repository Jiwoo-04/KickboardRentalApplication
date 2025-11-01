import payment.*;

public class Rental {
    private Vehicle vehicle;
    private int minutes;

    public Rental(Vehicle vehicle, int minutes) {
        this.vehicle = vehicle;
        this.minutes = minutes;
    }

    public double processPayment(boolean useCoupon) {
        Payment payment = new BasicPayment(vehicle.getBaseFee());
        payment = new TimeBasedFee(payment, vehicle.getRatePerMinute(), minutes);

        if (useCoupon) {
            payment = new CouponDiscount(payment, 0.1); // 10% 할인
        }

        System.out.println("\n[" + vehicle.getName() + " 대여 결제]");
        double total = payment.pay();
        return total;
    }
}
