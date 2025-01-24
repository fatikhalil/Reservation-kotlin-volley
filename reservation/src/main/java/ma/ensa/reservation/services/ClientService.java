package ma.ensa.reservation.services;

import ma.ensa.reservation.models.Client;
import ma.ensa.reservation.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }
    public Client updateClient(Long id, Client updatedClient) {
        if (clientRepository.existsById(id)) {
            updatedClient.setId(id);
            return clientRepository.save(updatedClient);
        } else {
            throw new RuntimeException("Client not found");
        }
    }


    public void deleteClient(Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
        } else {
            throw new RuntimeException("Client not found");
        }
    }

    // Récupérer le client à partir du token
    public Client getClientByToken(String token) {
        // Implémentez la logique pour décoder le token et récupérer le client
        // Exemple simplifié : récupérer le client par email (à adapter selon votre système d'authentification)
        String email = extractEmailFromToken(token); // Implémentez cette méthode
        return clientRepository.findByEmail(email);
    }

    // Supprimer le client à partir du token
    public boolean deleteClientByToken(String token) {
        // Implémentez la logique pour décoder le token et supprimer le client
        String email = extractEmailFromToken(token); // Implémentez cette méthode
        Client client = clientRepository.findByEmail(email);
        if (client != null) {
            clientRepository.delete(client);
            return true;
        } else {
            return false;
        }
    }

    // Méthode pour extraire l'email du token (exemple simplifié)
    private String extractEmailFromToken(String token) {
        // Implémentez la logique pour décoder le token et extraire l'email
        // Exemple : si vous utilisez JWT, utilisez une bibliothèque pour décoder le token
        return "client@example.com"; // Remplacez par la logique réelle
    }

    // Mettre à jour le client à partir du token
    public Client updateClientByToken(String token, Client updatedClient) {
        String email = extractEmailFromToken(token); // Implémentez cette méthode
        Client client = clientRepository.findByEmail(email);
        if (client != null) {
            client.setNom(updatedClient.getNom());
            client.setPrenom(updatedClient.getPrenom());
            client.setEmail(updatedClient.getEmail());
            client.setTelephone(updatedClient.getTelephone());
            client.setPassword(updatedClient.getPassword()); // En production, hash le mot de passe
            return clientRepository.save(client);
        } else {
            return null;
        }
    }

}