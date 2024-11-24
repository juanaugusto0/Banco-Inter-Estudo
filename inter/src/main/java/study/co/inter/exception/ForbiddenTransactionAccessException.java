package study.co.inter.exception;

public class ForbiddenTransactionAccessException extends Exception{

    public ForbiddenTransactionAccessException(Long transactionId, String name) {
        super("Transaction with id " + transactionId + " does not belong to " + name +".");
    }
}
