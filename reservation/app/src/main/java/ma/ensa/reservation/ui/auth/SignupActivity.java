package ma.ensa.reservation.ui.auth;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ma.ensa.reservation.api.ApiClient;
import ma.ensa.reservation.R;
import ma.ensa.reservation.api.ApiInterface;
import ma.ensa.reservation.api.AuthResponse;
import ma.ensa.reservation.models.AuthRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class SignupActivity extends AppCompatActivity {
    private EditText editTextNom, editTextPrenom, editTextEmail, editTextTelephone, editTextPassword;
    private Button buttonSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextNom = findViewById(R.id.editTextLastName);
        editTextPrenom = findViewById(R.id.editTextFirstName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextTelephone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignup = findViewById(R.id.buttonSignup);

        buttonSignup.setOnClickListener(v -> signupUser());
    }

    private void signupUser() {
        String nom = editTextNom.getText().toString().trim();
        String prenom = editTextPrenom.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String telephone = editTextTelephone.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telephone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthRequest authRequest = new AuthRequest();
        authRequest.setNom(nom);
        authRequest.setPrenom(prenom);
        authRequest.setEmail(email);
        authRequest.setTelephone(telephone);
        authRequest.setPassword(password);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AuthResponse> call = apiInterface.signup(authRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(SignupActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    finish(); // Retour à l'écran de connexion
                } else {
                    Toast.makeText(SignupActivity.this, "Échec de l'inscription", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}