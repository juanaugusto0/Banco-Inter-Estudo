package study.co.inter.controller;

import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/deposit")
    public String deposit (@RequestParam Long clientId, @RequestParam double depositAmount) {
        return transactionService.deposit(clientId, depositAmount);
    }

}
