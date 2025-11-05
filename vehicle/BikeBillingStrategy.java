package vehicle;

public class BikeBillingStrategy implements BillingStrategy {
    double TIME_FEE = 50;
    double BASE_FEE = 1000;

    @Override
    public double getBaseFee() {return BASE_FEE;}
    @Override
    public double getRatePerMinute() {return TIME_FEE;}

}
