package ma.ensa.reservation.repositories;

import ma.ensa.reservation.models.Chambre;
import ma.ensa.reservation.models.DispoChambre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChambreRepository extends JpaRepository<Chambre, Long> {
    List<Chambre> findByDispoChambre(DispoChambre dispoChambre); // Récupère les chambres par statut de disponibilité
}