package vehicle;

public class Bike extends Vehicle {
    public Bike(String id){
        super(id, "bike", true,new BikeBillingStrategy());
    }
}
