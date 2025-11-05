package vehicle;

public interface BillingStrategy {
    double TIME_FEE=0;
    double BASE_FEE=0;
    double getBaseFee();
    double getRatePerMinute();
}
