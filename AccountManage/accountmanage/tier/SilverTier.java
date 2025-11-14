package AccountManage.accountmanage.tier;

public class SilverTier implements Tier {

    @Override
    public String getName() { return "SILVER"; }

    @Override
    public double getDiscountRate() { return 0.10; }

    @Override
    public boolean isHigherThan(Tier other) {
        return !(other instanceof GoldTier || other instanceof SilverTier);
    }
}
