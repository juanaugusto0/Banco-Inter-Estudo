package study.co.inter.exception;

public class ClientCpfNotFoundException extends RuntimeException{

    public ClientCpfNotFoundException(String cpf) {
        super("Cliente com cpf " + cpf + " não encontrado.");
    }
}
