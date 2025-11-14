package AccountManage.accountmanage.tier;

public class NormalTier implements Tier {

    @Override
    public String getName() { return "NORMAL"; }

    @Override
    public double getDiscountRate() { return 0.0; }

    @Override
    public boolean isHigherThan(Tier other) {
        return (other instanceof NormalTier) == false;
    }
}