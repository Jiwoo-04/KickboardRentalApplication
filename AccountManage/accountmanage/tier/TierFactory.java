package AccountManage.accountmanage.tier;

public class TierFactory {

    // 총 사용 금액 기반 티어 생성
    public static Tier create(double totalSpent) {
        if (totalSpent >= 1000000) {  // 100만원 이상
            return new GoldTier();
        } else if (totalSpent >= 500000) { // 50만원 이상
            return new SilverTier();
        } else {
            return new NormalTier();
        }
    }

    // 문자열 기반 티어 복구
    public static Tier fromString(String tierName) {
        if (tierName == null) return null;

        switch (tierName.toUpperCase()) {
            case "GOLD": return new GoldTier();
            case "SILVER": return new SilverTier();
            case "NORMAL": return new NormalTier();
            default: return null;
        }
    }
}
