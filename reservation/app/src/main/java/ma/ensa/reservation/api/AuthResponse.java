package ma.ensa.reservation.api;
public class AuthResponse {
    private String token; // Token JWT (si vous utilisez l'authentification)
    private String message; // Message de réponse

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}