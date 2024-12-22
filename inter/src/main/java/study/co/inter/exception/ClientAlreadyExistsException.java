package study.co.inter.exception;

public class ClientAlreadyExistsException extends RuntimeException {

    public ClientAlreadyExistsException(Long cpf) {
        super("Cliente com cpf " + cpf + " já cadastrado.");
    }

}
