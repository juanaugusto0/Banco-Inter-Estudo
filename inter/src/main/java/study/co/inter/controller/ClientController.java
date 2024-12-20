package study.co.inter.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import study.co.inter.Service.ClientService;
import study.co.inter.dto.ClientDto;
import study.co.inter.model.Client;
import study.co.inter.model.Transaction;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/id/{id}")
    public String seeClientAccount(@PathVariable Long id) {
        return clientService.seeClientAccount(id);
    }

    @GetMapping("/balance/{cpf}")
    public String getBalance(@PathVariable Long cpf) {
        return clientService.getBalance(cpf);
    }

    @GetMapping("/cpf/{cpf}")
    public Client findClientByCpf(@PathVariable Long cpf) {
        return clientService.findClientByCpf(cpf);
    }

    @GetMapping("/transaction")
    public Transaction getTransactionById(@RequestParam Long transactionId, @RequestParam Long cpf) {
        return clientService.getTransactionById(transactionId, cpf);
    }

    @GetMapping("/transactions/{cpf}")
    public Set<Transaction> getTransactions(@PathVariable Long cpf) {
        return clientService.getTransactions(cpf);
    }

    @PostMapping("/add")
    public String addClient (@RequestBody ClientDto clientDto) {
        return clientService.addClient(clientDto);
    };

    @PutMapping("/update")
    public String updateClient (@RequestBody ClientDto clientDto) {
        return clientService.updateClient(clientDto);
    };

    @DeleteMapping("/remove/{cpf}")
    public String removeClient (@PathVariable Long cpf) {
        return clientService.removeClient(cpf);
    };



}
