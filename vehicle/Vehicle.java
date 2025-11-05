package vehicle;
import vehicle.BillingStrategy;

public abstract class Vehicle {
    protected String id;
    String type;
    String status;
    BillingStrategy billingStrategy;
    double basefee;
    double ratePerMinute;


    public Vehicle(String id, String type, BillingStrategy billingStrategy) {
        this.id = id;
        this.type = type;
        this.basefee = billingStrategy.getBaseFee();
        this.ratePerMinute = billingStrategy.getRatePerMinute();
        this.status = "available";
        this.billingStrategy = billingStrategy;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public double getBasefee() {
        return basefee;
    }

    public double getRatePerMinute() {
        return ratePerMinute;
    }
}
