package study.co.inter.Service;


import java.math.BigDecimal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import study.co.inter.dto.ClientDto;
import study.co.inter.exception.ClientAlreadyExistsException;
import study.co.inter.exception.ClientCpfNotFoundException;
import study.co.inter.exception.EmailAlreadyTakenException;
import study.co.inter.exception.NullDataException;
import study.co.inter.model.Client;
import study.co.inter.model.Transaction;
import study.co.inter.repository.ClientRepository;

@Service
public class ClientService {
    
    @Autowired
    private ClientRepository clientRepository;

    public Client findClientByCpf(String cpf) {
        Client client = clientRepository.findByCpf(cpf).orElse(null);
        if (client == null) {
            throw new ClientCpfNotFoundException(cpf);
        }
        if (client.getCpf() == null || Long.parseLong(client.getCpf()) <= 0) {
            throw new NullDataException();
        }
        return client;
    }

    public String addClient(ClientDto clientDto) {
        Client client = new Client();
        validateNewClient(clientDto);
        BigDecimal balance = new BigDecimal(0);
        client.setBalance(balance);
        client.setEmail(clientDto.getEmail());
        client.setMembershipTier(clientDto.getMembershipTier());
        client.setName(clientDto.getName());
        client.setCpf(clientDto.getCpf());
        clientRepository.save(client);
        return client.getName() + " adicionado(a) com sucesso";
    }

    public String updateClient(ClientDto clientDto){
        Client client = findClientByCpf(clientDto.getCpf());
        if (clientDto.getEmail() != null) client.setEmail(clientDto.getEmail());
        if (clientDto.getMembershipTier() != null) client.setMembershipTier(clientDto.getMembershipTier());
        if (clientDto.getName() != null) client.setName(clientDto.getName());

        validateUpdate(client);

        clientRepository.save(client);
        return client.getName() + " atualizado(a) com sucesso";

    }

    public String removeClient(String cpf){
        Client client = findClientByCpf(cpf);
        clientRepository.delete(client);
        return client.getName() + " removido(a) com sucesso";
    }

    public Set<Transaction> getTransactions(String cpf) {
        Client client = findClientByCpf(cpf);
        return client.getTransactions();
    }

    public void validateNewClient (ClientDto clientDto){
        if (clientDto.getCpf() == null || Long.parseLong(clientDto.getCpf()) <= 0 || clientDto.getEmail() == null || clientDto.getName() == null || clientDto.getMembershipTier() == null) {
            throw new NullDataException();
        }
        for (Client client : clientRepository.findAll()) {
            if (client.getCpf().equals(clientDto.getCpf())) {
                throw new ClientAlreadyExistsException(clientDto.getCpf());
            }
        }
        for (Client client : clientRepository.findAll()) {
            if (client.getEmail().equals(clientDto.getEmail())) {
                throw new EmailAlreadyTakenException(clientDto.getEmail());
            }
        }
    }

    public void validateUpdate(Client client) {
        for (Client c : clientRepository.findAll()) {
            if (client.getEmail().equals(c.getEmail()) && !client.equals(c)) {
                throw new EmailAlreadyTakenException(client.getEmail());
            }
        }
    }
    
}
