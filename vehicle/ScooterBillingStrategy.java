package vehicle;

public class ScooterBillingStrategy implements BillingStrategy {
    double TIME_FEE = 100;
    double BASE_FEE = 3000;

    @Override
    public double getRatePerMinute() {return TIME_FEE;}
    @Override
    public double getBaseFee() {return BASE_FEE;}
}
