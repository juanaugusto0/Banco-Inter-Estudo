package study.co.inter.exception;

public class ClientIdNotFoundException extends RuntimeException {
    
    public ClientIdNotFoundException(Long id) {
        super("Cliente com id " + id + " não encontrado.");
    }
}
