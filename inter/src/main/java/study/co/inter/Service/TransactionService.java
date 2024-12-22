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
import study.co.inter.exception.TransferToItselfException;
import study.co.inter.model.Client;
import study.co.inter.model.Transaction;
import study.co.inter.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private ClientService clientService;
    
    @Autowired
    private TransactionRepository transactionRepository;

    public int convertBigDecimalToInt(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        return value.intValue();
    }

    private void saveTransaction(TransactionType type, BigDecimal amount, Long clientCpf) {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setClient(clientService.findClientByCpf(clientCpf));
        transactionRepository.save(transaction);
    }

    private void validateTransfer(Client sender, Long recipient, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidValueException(amount.doubleValue());
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(sender.getBalance().doubleValue(), amount.doubleValue());
        }
        if (amount.compareTo(BigDecimal.valueOf(sender.getMembershipTier().getLimit())) > 0) {
            throw new AccountTierLimitException(sender.getMembershipTier(), sender.getName());
        }
        if (sender.getCpf().equals(recipient)) {
            throw new TransferToItselfException();
        }
    }

    public String deposit(Long clientCpf, BigDecimal depositAmount) {
        validateDeposit(depositAmount);
        
        Client client = clientService.findClientByCpf(clientCpf);
        client.setBalance(client.getBalance().add(depositAmount));
        ClientDto clientDto = new ClientDto(
            client.getName(),
            client.getMembershipTier(),
            client.getEmail(),
            client.getCpf()
        );
        clientService.updateClient(clientDto);
        saveTransaction(TransactionType.DEPOSITO, depositAmount, clientCpf);
        return "Depósito de R$" + depositAmount + " feito com sucesso";
    }

    public void validateDeposit(BigDecimal depositAmount) {
        if (depositAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidValueException(depositAmount.doubleValue());
        }
        int depositIntAmount = convertBigDecimalToInt(depositAmount);
        if (depositIntAmount % 5 != 0 && depositIntAmount % 2 != 0) {
            throw new InvalidValueException(depositAmount.doubleValue());
        }
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
        saveTransaction(TransactionType.SAQUE, withdrawalAmount, clientCpf);
        return "Saque de R$" + withdrawalAmount + " feito com sucesso";
    }

    private void validateWithdrawal(Long clientCpf, BigDecimal withdrawalAmount) {
        if (withdrawalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidValueException(withdrawalAmount.doubleValue());
        }
        Client client = clientService.findClientByCpf(clientCpf);
        if (client.getBalance().compareTo(withdrawalAmount) < 0) {
            throw new InsufficientFundsException(client.getBalance().doubleValue(), withdrawalAmount.doubleValue());
        }
        int withdrawalIntAmount = convertBigDecimalToInt(withdrawalAmount);
        if (withdrawalIntAmount % 5 != 0 && withdrawalIntAmount % 2 != 0) {
            throw new InvalidValueException(withdrawalAmount.doubleValue());
        }
    }

    public String transfer(Long senderCpf, Long recipientCpf, BigDecimal amount) {
        Client sender = clientService.findClientByCpf(senderCpf);
        Client recipient = clientService.findClientByCpf(recipientCpf);

        validateTransfer(sender, recipientCpf, amount);

        sender.setBalance(sender.getBalance().subtract(amount));
        recipient.setBalance(recipient.getBalance().add(amount));
        
        ClientDto senderDto = new ClientDto(sender.getName(), sender.getMembershipTier(), sender.getEmail(), sender.getCpf());
        ClientDto recipientDto = new ClientDto(recipient.getName(), recipient.getMembershipTier(), recipient.getEmail(), recipient.getCpf());
        clientService.updateClient(senderDto);
        clientService.updateClient(recipientDto);

        saveTransaction(TransactionType.TRANSFERENCIA, amount, senderCpf);
        saveTransaction(TransactionType.TRANSFERENCIA, amount, recipientCpf);

        return "Transferência de R$" + amount + " feita com sucesso para " + recipient.getName();
    }

}
