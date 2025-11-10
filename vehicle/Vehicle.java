package vehicle;
import vehicle.BillingStrategy;

public class Vehicle {
    protected String id;
    String type;
    Boolean status;
    BillingStrategy billingStrategy;
    double basefee;
    double ratePerMinute;

    public Vehicle(String id, String type, Boolean status,BillingStrategy billingStrategy) {
        this.id = id;
        this.type = type;
        this.basefee = billingStrategy.getBaseFee();
        this.ratePerMinute = billingStrategy.getRatePerMinute();
        this.status = status;
        this.billingStrategy = billingStrategy;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Boolean getStatus() {
        return status;
    }

    public double getBasefee() {
        return basefee;
    }

    public double getRatePerMinute() {
        return ratePerMinute;
    }
}
