package ma.ensa.reservation.ui.reservations;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import ma.ensa.reservation.R;
import ma.ensa.reservation.api.ApiClient;
import ma.ensa.reservation.api.ApiInterface;
import ma.ensa.reservation.models.Chambre;
import ma.ensa.reservation.models.Client;
import ma.ensa.reservation.models.ReservationInput;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReservationActivity extends AppCompatActivity {

    private EditText editTextClientId, editTextDateDebut, editTextDateFin, editTextPreferences;
    private Spinner spinnerChambres;
    private Button buttonSaveReservation;
    private Long reservationId; // Pour stocker l'ID de la réservation en mode édition

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);

        // Initialiser les vues
        editTextClientId = findViewById(R.id.editClientId);
        editTextDateDebut = findViewById(R.id.editDateDebut);
        editTextDateFin = findViewById(R.id.editDateFin);
        editTextPreferences = findViewById(R.id.editPreferences);
        spinnerChambres = findViewById(R.id.spinnerChambre);
        buttonSaveReservation = findViewById(R.id.btnSaveReservation);

        // Récupérer l'ID de la réservation depuis l'intent (si en mode édition)
        reservationId = getIntent().getLongExtra("reservationId", -1);

        // Charger les chambres disponibles
        loadAvailableChambres();

        // Si en mode édition, charger les détails de la réservation
        if (reservationId != -1) {
            loadReservationDetails(reservationId);
        }

        // Gérer le clic sur le bouton "Sauvegarder Réservation"
        buttonSaveReservation.setOnClickListener(v -> {
            if (reservationId != -1) {
                updateReservation(reservationId); // Mode édition
            } else {
                saveReservation(); // Mode création
            }
        });
    }

    /**
     * Charge les chambres disponibles depuis l'API et les affiche dans le Spinner.
     */
    private void loadAvailableChambres() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Chambre>> call = apiService.getAvailableChambres(); // Appel à l'endpoint des chambres disponibles

        call.enqueue(new Callback<List<Chambre>>() {
            @Override
            public void onResponse(Call<List<Chambre>> call, Response<List<Chambre>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Chambre> chambres = response.body();

                    // Créer un adaptateur pour le Spinner
                    ArrayAdapter<Chambre> adapter = new ArrayAdapter<>(
                            AddReservationActivity.this,
                            android.R.layout.simple_spinner_item,
                            chambres
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerChambres.setAdapter(adapter);
                } else {
                    Toast.makeText(AddReservationActivity.this, "Failed to load available chambres", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Chambre>> call, Throwable t) {
                Toast.makeText(AddReservationActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Charge les détails d'une réservation existante pour les afficher dans le formulaire.
     */
    private void loadReservationDetails(Long reservationId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ReservationInput> call = apiService.getReservationById(reservationId);

        call.enqueue(new Callback<ReservationInput>() {
            @Override
            public void onResponse(Call<ReservationInput> call, Response<ReservationInput> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReservationInput reservationInput = response.body();

                    // Remplir les champs du formulaire avec les détails de la réservation
                    editTextClientId.setText(String.valueOf(reservationInput.getClient().getId()));
                    editTextDateDebut.setText(reservationInput.getDateDebut());
                    editTextDateFin.setText(reservationInput.getDateFin());
                    editTextPreferences.setText(reservationInput.getPreferences());

                    // Sélectionner la chambre correspondante dans le Spinner
                    for (int i = 0; i < spinnerChambres.getCount(); i++) {
                        Chambre chambre = (Chambre) spinnerChambres.getItemAtPosition(i);
                        if (chambre.getId().equals(reservationInput.getChambre().getId())) {
                            spinnerChambres.setSelection(i);
                            break;
                        }
                    }
                } else {
                    Toast.makeText(AddReservationActivity.this, "Failed to load reservation details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReservationInput> call, Throwable t) {
                Toast.makeText(AddReservationActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Sauvegarde une nouvelle réservation en utilisant les données saisies par l'utilisateur.
     */
    private void saveReservation() {
        // Vérifier que les champs ne sont pas vides
        if (editTextClientId.getText().toString().isEmpty() ||
                editTextDateDebut.getText().toString().isEmpty() ||
                editTextDateFin.getText().toString().isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Valider le format des dates
        if (!isValidDate(editTextDateDebut.getText().toString()) || !isValidDate(editTextDateFin.getText().toString())) {
            Toast.makeText(this, "Format de date invalide. Utilisez yyyy-MM-dd", Toast.LENGTH_SHORT).show();
            return;
        }

        // Récupérer les données saisies
        Long clientId = Long.parseLong(editTextClientId.getText().toString());
        Chambre selectedChambre = (Chambre) spinnerChambres.getSelectedItem();

        // Vérifier qu'une chambre est sélectionnée
        if (selectedChambre == null) {
            Toast.makeText(this, "Veuillez sélectionner une chambre", Toast.LENGTH_SHORT).show();
            return;
        }

        // Créer des objets Client et Chambre
        Client client = new Client();
        client.setId(clientId);

        Chambre chambre = new Chambre();
        chambre.setId(selectedChambre.getId());

        String dateDebut = editTextDateDebut.getText().toString();
        String dateFin = editTextDateFin.getText().toString();
        String preferences = editTextPreferences.getText().toString();

        // Créer un objet ReservationInput avec les objets complets
        ReservationInput reservationInput = new ReservationInput(client, chambre, dateDebut, dateFin, preferences);

        // Envoyer la réservation à l'API
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ReservationInput> call = apiService.createReservation(reservationInput);
        call.enqueue(new Callback<ReservationInput>() {
            @Override
            public void onResponse(Call<ReservationInput> call, Response<ReservationInput> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddReservationActivity.this, "Reservation saved successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Fermer l'activité après la sauvegarde
                } else {
                    // Afficher le message d'erreur retourné par l'API
                    String errorMessage = "Failed to save reservation: ";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += response.errorBody().string();
                        } catch (Exception e) {
                            errorMessage += "Unknown error";
                            e.printStackTrace();
                        }
                    } else {
                        errorMessage += "No error body";
                    }
                    Toast.makeText(AddReservationActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ReservationInput> call, Throwable t) {
                Toast.makeText(AddReservationActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Met à jour une réservation existante en utilisant les données saisies par l'utilisateur.
     */
    private void updateReservation(Long reservationId) {
        // Vérifier que les champs ne sont pas vides
        if (editTextClientId.getText().toString().isEmpty() ||
                editTextDateDebut.getText().toString().isEmpty() ||
                editTextDateFin.getText().toString().isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Valider le format des dates
        if (!isValidDate(editTextDateDebut.getText().toString()) || !isValidDate(editTextDateFin.getText().toString())) {
            Toast.makeText(this, "Format de date invalide. Utilisez yyyy-MM-dd", Toast.LENGTH_SHORT).show();
            return;
        }

        // Récupérer les données saisies
        Long clientId = Long.parseLong(editTextClientId.getText().toString());
        Chambre selectedChambre = (Chambre) spinnerChambres.getSelectedItem();

        // Vérifier qu'une chambre est sélectionnée
        if (selectedChambre == null) {
            Toast.makeText(this, "Veuillez sélectionner une chambre", Toast.LENGTH_SHORT).show();
            return;
        }

        // Créer des objets Client et Chambre
        Client client = new Client();
        client.setId(clientId);

        Chambre chambre = new Chambre();
        chambre.setId(selectedChambre.getId());

        String dateDebut = editTextDateDebut.getText().toString();
        String dateFin = editTextDateFin.getText().toString();
        String preferences = editTextPreferences.getText().toString();

        // Créer un objet ReservationInput avec les objets complets
        ReservationInput reservationInput = new ReservationInput(client, chambre, dateDebut, dateFin, preferences);

        // Envoyer la réservation à l'API
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ReservationInput> call = apiService.updateReservation(reservationId, reservationInput);

        call.enqueue(new Callback<ReservationInput>() {
            @Override
            public void onResponse(Call<ReservationInput> call, Response<ReservationInput> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddReservationActivity.this, "Reservation saved successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Fermer l'activité après la sauvegarde
                } else {
                    // Afficher le message d'erreur retourné par l'API
                    String errorMessage = "Failed to save reservation: ";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += response.errorBody().string();
                        } catch (Exception e) {
                            errorMessage += "Unknown error";
                            e.printStackTrace();
                        }
                    } else {
                        errorMessage += "No error body";
                    }
                    Toast.makeText(AddReservationActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ReservationInput> call, Throwable t) {
                Toast.makeText(AddReservationActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Valide le format de la date (yyyy-MM-dd).
     */
    private boolean isValidDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}