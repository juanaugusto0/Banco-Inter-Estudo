package study.co.inter.exception;

public class NullDataException extends RuntimeException {

    public NullDataException() {
        super("Dados nulos ou negativos não são permitidos.");
    }
    
}
