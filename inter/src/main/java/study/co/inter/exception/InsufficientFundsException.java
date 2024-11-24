package study.co.inter.exception;

public class InsufficientFundsException extends RuntimeException{
    
    public InsufficientFundsException(double currentBalance, double value) {
        super("Insufficient funds for this transaction. Current balance is " + currentBalance + " and the value typed is " + value + ".");
    }
}
