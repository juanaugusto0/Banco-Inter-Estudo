package study.co.inter.Service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import study.co.inter.dto.ClientDto;
import study.co.inter.enums.TransactionType;
import study.co.inter.exception.AccountTierLimitException;
import study.co.inter.exception.InsufficientFundsException;
import study.co.inter.exception.InvalidValueException;
import study.co.inter.model.Client;
import study.co.inter.model.Transaction;
import study.co.inter.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private ClientService clientService;
    
    @Autowired
    private TransactionRepository transactionRepository;

    private void saveTransaction(TransactionType type, double amount, Long clientId) {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(type);
        transaction.setAmount(amount);
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

    
    public String deposit(Long clientId, double depositAmount) {
        if (depositAmount <= 0) {
            throw new InvalidValueException(depositAmount);
        }
        Client client = clientService.findClientById(clientId);
        client.setBalance(client.getBalance() + depositAmount);
        ClientDto clientDto = new ClientDto(
            client.getName(),
            client.getMembershipTier(),
            client.getEmail(),
            client.getCpf()
        );
        clientService.updateClient(clientDto);
        saveTransaction(TransactionType.DEPOSIT, depositAmount, clientId);
        return "Deposit of $"+depositAmount+" made successfully";
    }

    public String withdraw(Long clientId, double withdrawalAmount) {
        validateWithdrawal(clientId, withdrawalAmount);
        Client client = clientService.findClientById(clientId);
        client.setBalance(client.getBalance() - withdrawalAmount);
        ClientDto clientDto = new ClientDto(
            client.getName(),
            client.getMembershipTier(),
            client.getEmail(),
            client.getCpf()
        );
        clientService.updateClient(clientDto);
        saveTransaction(TransactionType.WITHDRAWAL, withdrawalAmount, clientId);
        return "Withdrawal of $"+withdrawalAmount+" made successfully";
    }

    private void validateWithdrawal(Long clientId, double withdrawalAmount) {
        if (withdrawalAmount <= 0) {
            throw new InvalidValueException(withdrawalAmount);
        }
        Client client = clientService.findClientById(clientId);
        if (client.getBalance() < withdrawalAmount) {
            throw new InsufficientFundsException(client.getBalance(), withdrawalAmount);
        }
    }


    public String transfer(Long senderId, Long recipientId, double amount) {
        Client sender = clientService.findClientById(senderId);
        Client recipient = clientService.findClientById(recipientId);

        validateTransfer(sender, amount);

        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);
        
        ClientDto senderDto = new ClientDto(sender.getName(), sender.getMembershipTier(), sender.getEmail(), sender.getCpf());
        ClientDto recipientDto = new ClientDto(recipient.getName(), recipient.getMembershipTier(), recipient.getEmail(), recipient.getCpf());
        clientService.updateClient(senderDto);
        clientService.updateClient(recipientDto);

        saveTransaction(TransactionType.TRANSFER, amount, senderId);
        saveTransaction(TransactionType.TRANSFER, amount, recipientId);

        return "Transfer of $"+amount+" made successfully to "+recipient.getName();
    }

}
