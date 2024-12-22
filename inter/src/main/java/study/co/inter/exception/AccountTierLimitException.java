package study.co.inter.exception;

import study.co.inter.enums.MembershipTier;

public class AccountTierLimitException extends RuntimeException {
    public AccountTierLimitException(MembershipTier membershipTier, String sendingName) {
        super("Limite da conta " + membershipTier + " de R$" + membershipTier.getLimit() + " atingido para " + sendingName);
    }
}
