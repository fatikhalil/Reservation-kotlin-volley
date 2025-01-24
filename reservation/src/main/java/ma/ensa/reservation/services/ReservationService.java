package ma.ensa.reservation.services;

import ma.ensa.reservation.models.Client;
import ma.ensa.reservation.models.Reservation;
import ma.ensa.reservation.repositories.ChambreRepository;
import ma.ensa.reservation.repositories.ClientRepository;
import ma.ensa.reservation.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ma.ensa.reservation.models.Chambre;
import ma.ensa.reservation.models.ReservationInput;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientRepository clientRepository; // Ajout du ClientRepository


    @Autowired
    private ChambreRepository chambreRepository;


    // Récupérer toutes les réservations
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Créer une réservation
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    // Récupérer une réservation par ID
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    // Mettre à jour une réservation
    public Reservation updateReservation(Long id, Reservation updatedReservation) {
        return reservationRepository.findById(id).map(reservation -> {
            reservation.setClient(updatedReservation.getClient());
            reservation.setChambre(updatedReservation.getChambre());
            reservation.setDateDebut(updatedReservation.getDateDebut());
            reservation.setDateFin(updatedReservation.getDateFin());
            reservation.setPreferences(updatedReservation.getPreferences());
            return reservationRepository.save(reservation);
        }).orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    // Supprimer une réservation
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    // Ajouter un client
    public Client addClient(Client client) {
        return clientRepository.save(client); // Enregistrer le client dans la base de données
    }

    // Récupérer tous les clients (facultatif)
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Reservation createReservation(ReservationInput reservationInput) {
        // Récupérer le client et la chambre à partir des objets dans ReservationInput
        Client client = reservationInput.getClient();
        Chambre chambre = reservationInput.getChambre();

        // Créer une nouvelle réservation
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setChambre(chambre);
        reservation.setDateDebut(reservationInput.getDateDebut());
        reservation.setDateFin(reservationInput.getDateFin());
        reservation.setPreferences(reservationInput.getPreferences());

        // Sauvegarder la réservation
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Long id, ReservationInput updatedReservationInput) {
        // Récupérer la réservation existante
        Optional<Reservation> existingReservation = reservationRepository.findById(id);
        if (existingReservation.isPresent()) {
            Reservation reservation = existingReservation.get();

            // Mettre à jour les champs de la réservation
            reservation.setClient(updatedReservationInput.getClient());
            reservation.setChambre(updatedReservationInput.getChambre());
            reservation.setDateDebut(updatedReservationInput.getDateDebut());
            reservation.setDateFin(updatedReservationInput.getDateFin());
            reservation.setPreferences(updatedReservationInput.getPreferences());

            // Sauvegarder la réservation mise à jour
            return reservationRepository.save(reservation);
        } else {
            return null; // Ou lever une exception
        }
    }

}
