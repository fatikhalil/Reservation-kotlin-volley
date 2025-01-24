package ma.ensa.reservation.services;

import ma.ensa.reservation.models.AuthRequest;
import ma.ensa.reservation.models.AuthResponse;
import ma.ensa.reservation.models.Client;
import ma.ensa.reservation.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private ClientRepository clientRepository;

    // Méthode pour l'authentification
    public AuthResponse authenticate(AuthRequest authRequest) {
        Client client = clientRepository.findByEmail(authRequest.getEmail());
        if (client != null && client.getPassword().equals(authRequest.getPassword())) {
            // Authentification réussie
            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken("fake-jwt-token"); // Remplacez par un vrai token JWT si nécessaire
            authResponse.setMessage("Connexion réussie");
            return authResponse;
        } else {
            // Authentification échouée
            return null;
        }
    }

    // Méthode pour l'inscription
    public AuthResponse register(AuthRequest authRequest) {
        // Vérifier si l'email est déjà utilisé
        if (clientRepository.findByEmail(authRequest.getEmail()) != null) {
            return null; // Email déjà utilisé
        }

        // Créer un nouveau client
        Client client = new Client();
        client.setNom(authRequest.getNom());
        client.setPrenom(authRequest.getPrenom());
        client.setEmail(authRequest.getEmail());
        client.setTelephone(authRequest.getTelephone());
        client.setPassword(authRequest.getPassword()); // En production, hash le mot de passe
        clientRepository.save(client);

        // Retourner une réponse
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken("fake-jwt-token"); // Remplacez par un vrai token JWT si nécessaire
        authResponse.setMessage("Inscription réussie");
        return authResponse;
    }
}