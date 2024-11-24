package study.co.inter.exception;

public class ClientCpfNotFoundException extends RuntimeException{

    public ClientCpfNotFoundException(Long cpf) {
        super("Client with cpf " + cpf + " not found.");
    }
}
