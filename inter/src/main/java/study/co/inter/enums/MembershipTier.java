package study.co.inter.enums;

public enum MembershipTier {
    SILVER(1000.0), GOLD(5000.0), PLATINUM(10000.0), BLACK(Double.MAX_VALUE);

    private final double limit;

    MembershipTier(double limit) {
        this.limit = limit;
    }

    public double getLimit() {
        return limit;
    }

}
