package ma.ensa.reservation.services;

import ma.ensa.reservation.models.Chambre;
import ma.ensa.reservation.models.DispoChambre;
import ma.ensa.reservation.repositories.ChambreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChambreService {


    @Autowired
    private ChambreRepository chambreRepository;

    public List<Chambre> getAllChambres() {
        return chambreRepository.findAll();
    }

    public Chambre createChambre(Chambre chambre) {
        return chambreRepository.save(chambre);
    }

    public Optional<Chambre> getChambreById(Long id) {
        return chambreRepository.findById(id);
    }
    public Chambre updateChambre(Long id, Chambre updatedChambre) {
        if (chambreRepository.existsById(id)) {
            updatedChambre.setId(id);
            return chambreRepository.save(updatedChambre);
        } else {
            throw new RuntimeException("Chambre not found");
        }
    }

    public void deleteChambre(Long id) {
        if (chambreRepository.existsById(id)) {
            chambreRepository.deleteById(id);
        } else {
            throw new RuntimeException("Chambre not found");
        }
    }

    public List<Chambre> getAvailableChambres() {
        return chambreRepository.findByDispoChambre(DispoChambre.DISPONIBLE); // Récupère les chambres disponibles
    }
}