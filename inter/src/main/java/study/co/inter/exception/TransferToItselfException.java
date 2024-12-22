package study.co.inter.exception;

public class TransferToItselfException extends RuntimeException {

    public TransferToItselfException() {
        super("Não é possível transferir dinheiro para você mesmo.");
    }
}
