package study.co.inter.exception;

public class InsufficientFundsException extends RuntimeException{
    
    public InsufficientFundsException(double currentBalance, double value) {
        super("Fundos insuficientes para esta transação. O saldo atual é R$" + currentBalance + " e o valor solicitado é R$" + value + ".");
    }
}
