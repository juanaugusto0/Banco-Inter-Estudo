package study.co.inter.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import study.co.inter.dto.ClientDto;
import study.co.inter.exception.ClientCpfNotFoundException;
import study.co.inter.exception.ClientIdNotFoundException;
import study.co.inter.exception.ForbiddenTransactionAccessException;
import study.co.inter.exception.TransactionNotFoundException;
import study.co.inter.model.Client;
import study.co.inter.model.Transaction;
import study.co.inter.repository.ClientRepository;
import study.co.inter.repository.TransactionRepository;

@Service
public class ClientService {
    
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public String seeClientAccount(Long clientId) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            throw new ClientIdNotFoundException(clientId);
        }
        return client.toString();
    }

    public Client findClientByCpf(Long cpf) {
        Client client = clientRepository.findByCpf(cpf).orElse(null);
        if (client == null) {
            throw new ClientCpfNotFoundException(cpf);
        }
        return client;
    }

    public String addClient(ClientDto clientDto) {
        Client client = new Client();
        client.setEmail(clientDto.getEmail());
        client.setMembershipTier(clientDto.getMembershipTier());
        client.setName(clientDto.getName());
        client.setCpf(clientDto.getCpf());
        clientRepository.save(client);
        return client.getName() + " added successfully";
    }

    public String updateClient(ClientDto clientDto){
        Client client = findClientByCpf(clientDto.getCpf());
        if (clientDto.getEmail() != null) client.setEmail(clientDto.getEmail());
        if (clientDto.getMembershipTier() != null) client.setMembershipTier(clientDto.getMembershipTier());
        if (clientDto.getName() != null) client.setName(clientDto.getName());

        clientRepository.save(client);
        return client.getName() + " updated successfully\n"+client.toString();

    }

    public String removeClient(Long cpf){
        Client client = findClientByCpf(cpf);
        clientRepository.delete(client);
        return client.getName() + " removed successfully";
    }

    public String getBalance(Long cpf){
        Client client = findClientByCpf(cpf);
        return "$"+client.getBalance();
    }

    public Transaction getTransactionById(Long transactionId, Long cpf) {
        Client client = findClientByCpf(cpf);
        Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
        if (transaction == null) {
            throw new TransactionNotFoundException(transactionId);
        }
        if (!transaction.getClient().equals(client)) {
            throw new ForbiddenTransactionAccessException(transactionId, client.getName());
        }
        return transaction;
    }
    
}
