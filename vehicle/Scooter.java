package vehicle;

public class Scooter extends Vehicle {
    public Scooter(String id) {
        super(id,"scooter", true,new ScooterBillingStrategy());
    }
}
