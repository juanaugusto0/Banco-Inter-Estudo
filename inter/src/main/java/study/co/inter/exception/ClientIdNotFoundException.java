package study.co.inter.exception;

public class ClientIdNotFoundException extends RuntimeException {
    
    public ClientIdNotFoundException(Long id) {
        super("Client with id " + id + " not found.");
    }
}
