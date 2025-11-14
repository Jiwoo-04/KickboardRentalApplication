package AccountManage.accountmanage.tier;

public class TierFactory {

    // 총 사용 금액 기반 티어 생성
    public static Tier create(double totalSpent) {
        if (totalSpent >= 500000) {  // 50만원 이상
            return new GoldTier();
        } else if (totalSpent >= 300000) { // 30만원 이상
            return new SilverTier();
        } else {
            return new NormalTier();
        }
    }

    public static Tier fromString(String tierName) {
        if (tierName == null) return new NormalTier();

        return switch (tierName.toUpperCase()) {
            case "GOLD" -> new GoldTier();
            case "SILVER" -> new SilverTier();
            default -> new NormalTier();
        };
    }
}
