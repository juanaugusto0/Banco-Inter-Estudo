package study.co.inter.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import study.co.inter.dto.ClientDto;
import study.co.inter.enums.MembershipTier;
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

    public Client findClientById(Long clientId) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) {
            throw new NullPointerException("Client not found");
        }
        return client;
    }

    public Client findClientByCpf(Long cpf) {
        Client client = clientRepository.findByCpf(cpf).orElse(null);
        if (client == null) {
            throw new NullPointerException("Client not found");
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

    public String updateClient(Long clientId, String email, MembershipTier membershipTier, String name, Long cpf) {
        Client client = findClientByCpf(cpf);
        if (email != null) client.setEmail(email);
        if (membershipTier != null) client.setMembershipTier(membershipTier);
        if (name != null) client.setName(name);
        if (cpf != null) client.setCpf(cpf);
        clientRepository.save(client);
        return client.getName() + " updated successfully";

    }

    public String removeClient(Long cpf){
        Client client = findClientByCpf(cpf);
        clientRepository.delete(client);
        return client.getName() + " removed successfully";
    }

    public double getBalance(Long cpf){
        Client client = findClientByCpf(cpf);
        return client.getBalance();
    }

    public Transaction getTransactionById(Long transactionId, Long cpf) {
        Client client = findClientByCpf(cpf);
        Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
        if (transaction == null) {
            throw new NullPointerException("Transaction not found");
        }
        if (!transaction.getClient().equals(client)) {
            throw new IllegalArgumentException("Transaction does not belong to the client");
        }
        return transaction;
    }
    
}
