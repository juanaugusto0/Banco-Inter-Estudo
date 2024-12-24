package study.co.inter.controller;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import study.co.inter.Service.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    
    private final TransactionService transactionService;
    
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PutMapping("/deposit")
    public String deposit (@RequestParam String clientCpf, @RequestParam String depositAmount) {
        BigDecimal depositAmountBigDecimal = new BigDecimal(depositAmount);
        return transactionService.deposit(clientCpf, depositAmountBigDecimal);
    }

    @PutMapping("/withdraw")
    public String withdraw (@RequestParam String clientCpf, @RequestParam String withdrawalAmount) {
        BigDecimal withdrawalAmountBigDecimal = new BigDecimal(withdrawalAmount);
        return transactionService.withdraw(clientCpf, withdrawalAmountBigDecimal);
    }

    @PutMapping("/transfer")
    public String transfer (@RequestParam String senderCpf, @RequestParam String recipientCpf, @RequestParam String amount) {
        BigDecimal amountBigDecimal = new BigDecimal(amount);
        return transactionService.transfer(senderCpf, recipientCpf, amountBigDecimal);
    }

}