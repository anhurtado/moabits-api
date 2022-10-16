package com.moabits.api.controllers;

import com.moabits.api.entities.Client;
import com.moabits.api.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/client")
    public ResponseEntity<List<Client>> getList() {
        List<Client> clientList = clientService.list();
        return ResponseEntity.ok().body(clientList);
    }
}
