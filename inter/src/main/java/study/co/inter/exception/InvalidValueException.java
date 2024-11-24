package study.co.inter.exception;

public class InvalidValueException extends RuntimeException {
    
    public InvalidValueException(double value) {
        super("Value '"+ value +"' is invalid for this transaction.");
    }
}
