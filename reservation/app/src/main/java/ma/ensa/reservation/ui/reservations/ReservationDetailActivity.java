package ma.ensa.reservation.ui.reservations;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ma.ensa.reservation.R;
import ma.ensa.reservation.api.ApiClient;
import ma.ensa.reservation.api.ApiInterface;
import ma.ensa.reservation.models.ReservationInput;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationDetailActivity extends AppCompatActivity {

    private TextView textViewClientId, textViewChambreId, textViewDateDebut, textViewDateFin, textViewPreferences;
    private Button buttonEditReservation, buttonDeleteReservation;
    private Long reservationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);

        textViewClientId = findViewById(R.id.textViewClientId);
        textViewChambreId = findViewById(R.id.textViewChambreId);
        textViewDateDebut = findViewById(R.id.textViewDateDebut);
        textViewDateFin = findViewById(R.id.textViewDateFin);
        textViewPreferences = findViewById(R.id.textViewPreferences);
        buttonEditReservation = findViewById(R.id.buttonEditReservation);
        buttonDeleteReservation = findViewById(R.id.buttonDeleteReservation);

        // Récupérer l'ID de la réservation depuis l'intent
        reservationId = getIntent().getLongExtra("reservationId", -1);

        if (reservationId != -1) {
            loadReservationDetails(reservationId);
        }

        // Gérer le clic sur le bouton "Modifier Réservation"
        buttonEditReservation.setOnClickListener(v -> {
            Intent intent = new Intent(ReservationDetailActivity.this, AddReservationActivity.class);
            intent.putExtra("reservationId", reservationId);
            startActivity(intent);
        });

        // Gérer le clic sur le bouton "Supprimer Réservation"
        buttonDeleteReservation.setOnClickListener(v -> deleteReservation(reservationId));
    }

    /**
     * Charge les détails d'une réservation depuis l'API.
     */
    private void loadReservationDetails(Long reservationId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ReservationInput> call = apiService.getReservationById(reservationId);

        call.enqueue(new Callback<ReservationInput>() {
            @Override
            public void onResponse(Call<ReservationInput> call, Response<ReservationInput> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReservationInput reservationInput = response.body();
                    // Afficher les détails de la réservation
                    textViewClientId.setText("Client ID: " + reservationInput.getClient().getId());
                    textViewChambreId.setText("Chambre ID: " + reservationInput.getChambre().getId());
                    textViewDateDebut.setText("Date Début: " + reservationInput.getDateDebut());
                    textViewDateFin.setText("Date Fin: " + reservationInput.getDateFin());
                    textViewPreferences.setText("Préférences: " + reservationInput.getPreferences());
                } else {
                    Toast.makeText(ReservationDetailActivity.this, "Failed to load reservation details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReservationInput> call, Throwable t) {
                Toast.makeText(ReservationDetailActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Supprime une réservation existante.
     */
    private void deleteReservation(Long reservationId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Void> call = apiService.deleteReservation(reservationId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReservationDetailActivity.this, "Reservation deleted successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Fermer l'activité après la suppression
                } else {
                    Toast.makeText(ReservationDetailActivity.this, "Failed to delete reservation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ReservationDetailActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}