package ma.ensa.reservation.controllers;

import ma.ensa.reservation.models.Client;
import ma.ensa.reservation.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }


    @PostMapping
    public Client createClient(@RequestBody Client client) {
        return clientService.createClient(client);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client updatedClient) {
        Optional<Client> client = Optional.ofNullable(clientService.updateClient(id, updatedClient));
        return client
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/names")
    public List<String> getClientsNames() {
        List<Client> clients = clientService.getAllClients();
        List<String> clientNames = new ArrayList<>();

        for (Client client : clients) {
            // Concaténer nom et prénom pour chaque client
            clientNames.add(client.getNom() + " " + client.getPrenom());
        }

        return clientNames;
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getClientProfile(@RequestHeader("Authorization") String authorizationHeader) {
        // Vérifiez si le header Authorization est bien présent et commence par "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid or missing Authorization header");
        }

        // Extraire le token en enlevant le préfixe "Bearer "
        String token = authorizationHeader.substring(7);

        try {
            // Récupérer le client en fonction du token
            Client client = clientService.getClientByToken(token);

            // Si le client est trouvé, renvoyer une réponse OK avec les informations du client
            if (client != null) {
                return ResponseEntity.ok(client);
            } else {
                // Si le client n'est pas trouvé pour ce token, renvoyer une erreur 404
                return ResponseEntity.status(404).body("Client not found for the provided token");
            }
        } catch (Exception e) {
            // En cas d'erreur, renvoyer une erreur 500 avec le message de l'exception
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    // Supprimer le profil du client connecté
    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteClientProfile(@RequestHeader("Authorization") String token) {
        // Implémentez la logique pour supprimer le client à partir du token
        boolean isDeleted = clientService.deleteClientByToken(token);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Mettre à jour le profil du client connecté
    @PutMapping("/profile")
    public ResponseEntity<Client> updateClientProfile(@RequestHeader("Authorization") String token, @RequestBody Client updatedClient) {
        Client client = clientService.updateClientByToken(token, updatedClient);
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}