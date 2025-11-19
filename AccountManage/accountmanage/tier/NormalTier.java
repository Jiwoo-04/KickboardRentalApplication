package AccountManage.accountmanage.tier;

public class NormalTier implements Tier {

    @Override
    public String getName() { return "NORMAL"; }

    @Override
    public double getDiscountRate() { return 0.0; }
}