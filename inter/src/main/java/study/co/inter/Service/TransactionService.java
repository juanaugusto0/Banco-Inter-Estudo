package study.co.inter.Service;

import java.math.BigDecimal;
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

    private void saveTransaction(TransactionType type, BigDecimal amount, Long clientCpf) {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setClient(clientService.findClientByCpf(clientCpf));
        transactionRepository.save(transaction);
    }

    private void validateTransfer(Client sender, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidValueException(amount.doubleValue());
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(sender.getBalance().doubleValue(), amount.doubleValue());
        }
        if (amount.compareTo(BigDecimal.valueOf(sender.getMembershipTier().getLimit())) > 0) {
            throw new AccountTierLimitException(sender.getMembershipTier(), sender.getName());
        }
    }

    public String deposit(Long clientCpf, BigDecimal depositAmount) {
        if (depositAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidValueException(depositAmount.doubleValue());
        }
        Client client = clientService.findClientByCpf(clientCpf);
        client.setBalance(client.getBalance().add(depositAmount));
        ClientDto clientDto = new ClientDto(
            client.getName(),
            client.getMembershipTier(),
            client.getEmail(),
            client.getCpf()
        );
        clientService.updateClient(clientDto);
        saveTransaction(TransactionType.DEPOSIT, depositAmount, clientCpf);
        return "Deposit of $" + depositAmount + " made successfully";
    }

    public String withdraw(Long clientCpf, BigDecimal withdrawalAmount) {
        validateWithdrawal(clientCpf, withdrawalAmount);
        Client client = clientService.findClientByCpf(clientCpf);
        client.setBalance(client.getBalance().subtract(withdrawalAmount));
        ClientDto clientDto = new ClientDto(
            client.getName(),
            client.getMembershipTier(),
            client.getEmail(),
            client.getCpf()
        );
        clientService.updateClient(clientDto);
        saveTransaction(TransactionType.WITHDRAWAL, withdrawalAmount, clientCpf);
        return "Withdrawal of $" + withdrawalAmount + " made successfully";
    }

    private void validateWithdrawal(Long clientCpf, BigDecimal withdrawalAmount) {
        if (withdrawalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidValueException(withdrawalAmount.doubleValue());
        }
        Client client = clientService.findClientByCpf(clientCpf);
        if (client.getBalance().compareTo(withdrawalAmount) < 0) {
            throw new InsufficientFundsException(client.getBalance().doubleValue(), withdrawalAmount.doubleValue());
        }
    }

    public String transfer(Long senderCpf, Long recipientCpf, BigDecimal amount) {
        Client sender = clientService.findClientByCpf(senderCpf);
        Client recipient = clientService.findClientByCpf(recipientCpf);

        validateTransfer(sender, amount);

        sender.setBalance(sender.getBalance().subtract(amount));
        recipient.setBalance(recipient.getBalance().add(amount));
        
        ClientDto senderDto = new ClientDto(sender.getName(), sender.getMembershipTier(), sender.getEmail(), sender.getCpf());
        ClientDto recipientDto = new ClientDto(recipient.getName(), recipient.getMembershipTier(), recipient.getEmail(), recipient.getCpf());
        clientService.updateClient(senderDto);
        clientService.updateClient(recipientDto);

        saveTransaction(TransactionType.TRANSFER, amount, senderCpf);
        saveTransaction(TransactionType.TRANSFER, amount, recipientCpf);

        return "Transfer of $" + amount + " made successfully to " + recipient.getName();
    }

}
