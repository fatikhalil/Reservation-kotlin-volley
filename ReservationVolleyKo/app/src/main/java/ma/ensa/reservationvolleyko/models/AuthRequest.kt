package ma.ensa.reservationvolleyko.models

class AuthRequest {
    var nom: String? = null
    var prenom: String? = null
    var email: String? = null
    var telephone: String? = null
    var password: String? = null

    // Constructeur par défaut
    constructor()

    // Constructeur avec email et password
    constructor(email: String, password: String) {
        this.email = email
        this.password = password
    }
}