package com.moabits.api.services;

import com.moabits.api.entities.Client;
import com.moabits.api.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public List<Client> list() {
        return clientRepository.findAll();
    }

    public Client getById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.isPresent() ? client.get() : null;
    }
}
