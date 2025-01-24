package ma.ensa.reservationvolleyko.models

class Chambre {
    var id: Long? = null
    var typeChambre: TypeChambre? = null
    var prix: Double = 0.0
    var dispoChambre: DispoChambre? = null

    // Constructeur par défaut
    constructor()

    // Constructeur avec paramètres
    constructor(id: Long?, typeChambre: TypeChambre?, prix: Double, dispoChambre: DispoChambre?) {
        this.id = id
        this.typeChambre = typeChambre
        this.prix = prix
        this.dispoChambre = dispoChambre
    }

    // Redéfinition de la méthode toString pour afficher le type et le prix
    override fun toString(): String {
        return "${typeChambre} - ${prix}€" // Affiche le type et le prix dans le Spinner
    }
}