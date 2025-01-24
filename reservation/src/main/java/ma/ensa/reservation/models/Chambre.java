package ma.ensa.reservation.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Chambre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeChambre typeChambre;

    private double prix;

    @Enumerated(EnumType.STRING)
    private DispoChambre dispoChambre;


    public Chambre() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DispoChambre getDispoChambre() {
        return dispoChambre;
    }

    public void setDispoChambre(DispoChambre dispoChambre) {
        this.dispoChambre = dispoChambre;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public TypeChambre getTypeChambre() {
        return typeChambre;
    }

    public void setTypeChambre(TypeChambre typeChambre) {
        this.typeChambre = typeChambre;
    }

    public Chambre(Long chambreId) {
        this.id = chambreId;
    }
}
