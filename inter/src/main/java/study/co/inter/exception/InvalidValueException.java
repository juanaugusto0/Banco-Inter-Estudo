package study.co.inter.exception;

public class InvalidValueException extends RuntimeException {
    
    public InvalidValueException(double value) {
        super("Valor '"+ value +"' é inválido para essa transação.");
    }
}
