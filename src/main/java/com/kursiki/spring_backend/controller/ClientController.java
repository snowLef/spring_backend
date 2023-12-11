package com.kursiki.spring_backend.controller;

import com.kursiki.spring_backend.ClientRepository;
import com.kursiki.spring_backend.model.Client;
import com.kursiki.spring_backend.model.ClientRequest;
import com.kursiki.spring_backend.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping(value = "/clients")
    public ResponseEntity<String> create(@RequestBody ClientRequest clientRequest) {
        Client client = new Client();
        client.setName(clientRequest.getName());
        client.setEmail(clientRequest.getEmail());
        client.setPhone(clientRequest.getPhone());

        clientRepository.save(client);
        return ResponseEntity.ok("User created successfully. Id: " + client.getId());
    }

    @GetMapping(value = "/clients")
    public ResponseEntity<List<Client>> read() {
        final List<Client> clients = clientRepository.findAll();

        return !clients.isEmpty()
                ? new ResponseEntity<>(clients, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/clients/{id}")
    public ResponseEntity<Client> read(@PathVariable(name = "id") long id) {
        final Client client = clientRepository.getOne(id);

        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @PutMapping(value = "/clients/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") long id, @RequestBody ClientRequest clientRequest) {
        Optional<Client> clientOptional = clientRepository.findById(id);

        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();

            // Обновляем поля пользователя с новыми данными из userRequest
            client.setName(clientRequest.getName());
            client.setEmail(clientRequest.getEmail());
            client.setPhone(clientRequest.getPhone());

            // Сохраняем обновленного пользователя в базе данных
            clientRepository.save(client);
            return ResponseEntity.ok("User has been updated");
        } else {
            // Обработка случая, если пользователя с заданным id не существует
            return ResponseEntity.notFound().build();
            //throw new UserNotFoundException("User with id " + id + " not found");
        }
    }

    @DeleteMapping(value = "/clients/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") long id) {
        clientRepository.deleteById(id);
        return ResponseEntity.ok("User has been deleted");
    }
}
