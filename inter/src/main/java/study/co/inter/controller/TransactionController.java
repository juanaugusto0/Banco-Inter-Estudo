package study.co.inter.controller;

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
    public String deposit (@RequestParam Long clientCpf, @RequestParam double depositAmount) {
        return transactionService.deposit(clientCpf, depositAmount);
    }

    @PutMapping("/withdraw")
    public String withdraw (@RequestParam Long clientCpf, @RequestParam double withdrawalAmount) {
        return transactionService.withdraw(clientCpf, withdrawalAmount);
    }

    @PutMapping("/transfer")
    public String transfer (@RequestParam Long senderCpf, @RequestParam Long recipientCpf, @RequestParam double amount) {
        return transactionService.transfer(senderCpf, recipientCpf, amount);
    }

}
