package study.co.inter.exception;

import study.co.inter.enums.MembershipTier;

public class AccountTierLimitException extends RuntimeException {
    public AccountTierLimitException(MembershipTier membershipTier, String sendingName) {
        super("Account " + membershipTier + " limit of "+ membershipTier.getLimit() +" reached for " + sendingName);
    }
}
