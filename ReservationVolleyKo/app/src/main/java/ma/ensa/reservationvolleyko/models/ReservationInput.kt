package ma.ensa.reservationvolleyko.models

class ReservationInput {
    var id: Long = 0
    var client: Client? = null // Objet Client
    var chambre: Chambre? = null // Objet Chambre
    var dateDebut: String? = null
    var dateFin: String? = null
    var preferences: String? = null

    // Constructeur avec paramètres
    constructor(client: Client?, chambre: Chambre?, dateDebut: String?, dateFin: String?, preferences: String?) {
        this.client = client
        this.chambre = chambre
        this.dateDebut = dateDebut
        this.dateFin = dateFin
        this.preferences = preferences
    }

    // Constructeur par défaut
    constructor()
}