package AccountManage.accountmanage.tier;

public class SilverTier implements Tier {

    @Override
    public String getName() { return "SILVER"; }

    @Override
    public double getDiscountRate() { return 0.10; }
}
