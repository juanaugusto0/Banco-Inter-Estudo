package study.co.inter.exception;

public class ClientCpfNotFoundException extends RuntimeException{

    public ClientCpfNotFoundException(Long cpf) {
        super("Cliente com cpf " + cpf + " não encontrado.");
    }
}
