package study.co.inter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import study.co.inter.Service.ClientService;
import study.co.inter.dto.ClientDto;
import study.co.inter.model.Client;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/find/{id}")
    public Client findClientById(@PathVariable Long id) {
        return clientService.findByClientId(id);
    }

    @PutMapping("/add")
    public String addClient (@RequestBody ClientDto clientDto) {
        return clientService.addClient(clientDto);
    };



}
