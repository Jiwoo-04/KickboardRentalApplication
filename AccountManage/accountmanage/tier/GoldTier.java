package AccountManage.accountmanage.tier;


public class GoldTier implements Tier {

    @Override
    public String getName() { return "GOLD"; }

    @Override
    public double getDiscountRate() { return 0.20; }

    @Override
    public boolean isHigherThan(Tier other) {
        return !(other instanceof GoldTier);
    }
}