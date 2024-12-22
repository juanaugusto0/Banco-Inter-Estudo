package study.co.inter.exception;

public class TransactionNotFoundException extends RuntimeException{

    public TransactionNotFoundException(Long id) {
        super("Transação de id '" + id + "' não encontrada.");
    }
}
