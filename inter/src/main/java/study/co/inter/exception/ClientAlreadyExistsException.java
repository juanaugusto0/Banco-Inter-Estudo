package study.co.inter.exception;

public class ClientAlreadyExistsException extends RuntimeException {

    public ClientAlreadyExistsException(String cpf) {
        super("Cliente com cpf " + cpf + " já cadastrado.");
    }

}
