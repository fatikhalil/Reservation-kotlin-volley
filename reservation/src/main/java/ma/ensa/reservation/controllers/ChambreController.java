package ma.ensa.reservation.controllers;

import ma.ensa.reservation.models.Chambre;
import ma.ensa.reservation.services.ChambreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chambres")
public class ChambreController {


    @Autowired
    private ChambreService chambreService;

    @GetMapping
    public List<Chambre> getAllChambres() {
        return chambreService.getAllChambres();
    }

    @PostMapping
    public Chambre createChambre(@RequestBody Chambre chambre) {
        return chambreService.createChambre(chambre);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chambre> getChambreById(@PathVariable Long id) {
        return chambreService.getChambreById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chambre> updateChambre(@PathVariable Long id, @RequestBody Chambre updatedChambre) {
        Optional<Chambre> chambre = Optional.ofNullable(chambreService.updateChambre(id, updatedChambre));
        return chambre
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChambre(@PathVariable Long id) {
        try {
            chambreService.deleteChambre(id);
            return ResponseEntity.noContent().build(); // Renvoie un code 204 (No Content)
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Renvoie un code 400 (Bad Request)
        }
    }
    @GetMapping("/available")
    public List<Chambre> getAvailableChambres() {
        return chambreService.getAvailableChambres();
    }
}