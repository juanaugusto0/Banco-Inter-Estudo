package study.co.inter.exception;

public class ForbiddenTransactionAccessException extends RuntimeException{

    public ForbiddenTransactionAccessException(Long transactionId, String name) {
        super("Transação com id " + transactionId + " não pertence a " + name + ".");
    }
}
