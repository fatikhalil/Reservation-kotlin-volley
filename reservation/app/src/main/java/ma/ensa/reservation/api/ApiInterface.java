package ma.ensa.reservation.api;
import ma.ensa.reservation.models.AuthRequest;
import ma.ensa.reservation.models.Chambre;
import ma.ensa.reservation.models.Client;
import java.util.List;

import ma.ensa.reservation.models.ReservationInput;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    // Endpoint pour la connexion
    @POST("api/auth/login")
    Call<AuthResponse> login(@Body AuthRequest authRequest);

    // Endpoint pour l'inscription
    @POST("api/auth/signup")
    Call<AuthResponse> signup(@Body AuthRequest authRequest);




    @GET("/api/chambres")
    Call<List<Chambre>> getAllChambres();

    @POST("/api/chambres")
    Call<Chambre> createChambre(@Body Chambre chambre);

    @GET("/api/chambres/{id}")
    Call<Chambre> getChambreById(@Path("id") Long id);

    @PUT("/api/chambres/{id}")
    Call<Chambre> updateChambre(@Path("id") Long id, @Body Chambre chambre);

    @DELETE("/api/chambres/{id}")
    Call<Void> deleteChambre(@Path("id") Long id);

    @GET("/api/reservations")
    Call<List<ReservationInput>> getAllReservations();

    @POST("/api/reservations") // Assurez-vous que l'endpoint est correct
    Call<ReservationInput> createReservation(@Body ReservationInput reservationInput);

    @GET("/api/reservations/{id}")
    Call<ReservationInput> getReservationById(@Path("id") Long id);

    @PUT("/api/reservations/{id}")
    Call<ReservationInput> updateReservation(@Path("id") Long id, @Body ReservationInput reservationInput);

    @DELETE("/api/reservations/{id}")
    Call<Void> deleteReservation(@Path("id") Long id);

    @GET("api/chambres/available")
    Call<List<Chambre>> getAvailableChambres();
}

