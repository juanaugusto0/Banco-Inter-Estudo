package study.co.inter.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import study.co.inter.enums.TransactionType;
import study.co.inter.exception.AccountTierLimitException;
import study.co.inter.exception.InsufficientFundsException;
import study.co.inter.exception.InvalidValueException;
import study.co.inter.model.Client;
import study.co.inter.model.Transaction;
import study.co.inter.repository.TransactionRepository;

@Service
public class TransactionService {

    private ClientService clientService;
    
    @Autowired
    private TransactionRepository transactionRepository;

    private void saveTransaction(TransactionType type, double amount, Long clientId) {
        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setValue(amount);
        transaction.setClient(clientService.findClientById(clientId));
        transactionRepository.save(transaction);
    }

    private void validateTransfer(Client sender, double amount) {
        if (amount <= 0) {
            throw new InvalidValueException(amount);
        }
        if (sender.getBalance() < amount) {
            throw new InsufficientFundsException(sender.getBalance(), amount);
        }
        if (amount > sender.getMembershipTier().getLimit()) {
            throw new AccountTierLimitException(sender.getMembershipTier(), sender.getName());
        }
    }

    
    public String deposit(Long clientId, double amount) {
        if (amount <= 0) {
            throw new InvalidValueException(amount);
        }
        Client client = clientService.findClientById(clientId);
        client.setBalance(client.getBalance() + amount);
        clientService.updateClient(client.getId(), client.getEmail(), client.getMembershipTier(), client.getName(), client.getCpf());
        saveTransaction(TransactionType.DEPOSIT, amount, clientId);
        return "Deposit made successfully";
    }

    public String withdrawal(Long clientId, double amount) {
        if (amount <= 0) {
            throw new InvalidValueException(amount);
        }
        Client client = clientService.findClientById(clientId);
        if (client.getBalance() < amount) {
            throw new InsufficientFundsException(client.getBalance(), amount);
        }
        client.setBalance(client.getBalance() - amount);
        clientService.updateClient(client.getId(), client.getEmail(), client.getMembershipTier(), client.getName(), client.getCpf());
        saveTransaction(TransactionType.WITHDRAWAL, amount, clientId);
        return "Withdrawal made successfully";
    }

    public String transfer(Long senderId, Long recipientId, double amount) {
        Client sender = clientService.findClientById(senderId);
        Client recipient = clientService.findClientById(recipientId);

        validateTransfer(sender, amount);

        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        clientService.updateClient(senderId, sender.getEmail(), sender.getMembershipTier(), sender.getName(), sender.getCpf());
        clientService.updateClient(recipientId, recipient.getEmail(), recipient.getMembershipTier(), recipient.getName(), recipient.getCpf());

        saveTransaction(TransactionType.TRANSFER, amount, senderId);
        saveTransaction(TransactionType.TRANSFER, amount, recipientId);

        return "Transfer made successfully";
    }

}
