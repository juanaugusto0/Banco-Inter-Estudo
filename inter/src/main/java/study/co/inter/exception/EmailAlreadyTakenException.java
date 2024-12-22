package study.co.inter.exception;

public class EmailAlreadyTakenException extends RuntimeException {

    public EmailAlreadyTakenException(String email) {
        super("Email '" + email + "' já cadastrado.");
    }
    
}
