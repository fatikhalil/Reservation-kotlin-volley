package ma.ensa.reservation.models;
public class Chambre {
    private Long id;
    private TypeChambre typeChambre;
    private double prix;
    private DispoChambre dispoChambre;

    public Chambre() {
    }

    public Chambre(Long id, TypeChambre typeChambre, double prix, DispoChambre dispoChambre) {
        this.id = id;
        this.typeChambre = typeChambre;
        this.prix = prix;
        this.dispoChambre = dispoChambre;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeChambre getTypeChambre() {
        return typeChambre;
    }

    public void setTypeChambre(TypeChambre typeChambre) {
        this.typeChambre = typeChambre;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public DispoChambre getDispoChambre() {
        return dispoChambre;
    }

    public void setDispoChambre(DispoChambre dispoChambre) {
        this.dispoChambre = dispoChambre;
    }

    @Override
    public String toString() {
        return typeChambre + " - " + prix + "€"; // Affiche le type et le prix dans le Spinner
    }
}