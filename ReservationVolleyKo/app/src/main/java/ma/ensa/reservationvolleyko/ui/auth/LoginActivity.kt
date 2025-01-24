package ma.ensa.reservationvolleyko.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import ma.ensa.reservationvolleyko.R
import ma.ensa.reservationvolleyko.api.AuthResponse
import ma.ensa.reservationvolleyko.api.VolleySingleton
import ma.ensa.reservationvolleyko.models.AuthRequest
import ma.ensa.reservationvolleyko.ui.MainActivity
import ma.ensa.reservationvolleyko.utils.SharedPreferencesManager
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialiser les vues
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonSignup = findViewById(R.id.buttonSignup)

        // Gérer le clic sur le bouton de connexion
        buttonLogin.setOnClickListener { loginUser() }

        // Gérer le clic sur le bouton d'inscription
        buttonSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        // Récupérer les valeurs saisies par l'utilisateur
        val username = editTextUsername.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        // Vérifier que les champs ne sont pas vides
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        // Créer un objet AuthRequest
        val authRequest = AuthRequest(username, password)

        // Convertir l'objet AuthRequest en JSON
        val jsonBody = JSONObject().apply {
            put("email", authRequest.email)
            put("password", authRequest.password)
        }

        // URL de l'endpoint de connexion
        val url = "http://192.168.1.160:8082/api/auth/login"

        // Créer une requête POST avec Volley
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            Response.Listener<JSONObject> { response ->
                try {
                    // Désérialiser la réponse JSON en AuthResponse
                    val gson = Gson()
                    val authResponse = gson.fromJson(response.toString(), AuthResponse::class.java)

                    // Vérifier si le token est non null
                    authResponse.token?.let { token ->
                        // Stocker le token dans SharedPreferences
                        SharedPreferencesManager.getInstance(this@LoginActivity).saveToken(token)

                        // Afficher un message de succès
                        Toast.makeText(this@LoginActivity, "Connexion réussie", Toast.LENGTH_SHORT).show()

                        // Rediriger vers MainActivity
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } ?: run {
                        // Si le token est null, afficher un message d'erreur
                        Toast.makeText(this@LoginActivity, "Erreur: Token non reçu", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@LoginActivity, "Erreur lors de la désérialisation", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Gérer l'erreur
                Toast.makeText(this@LoginActivity, "Erreur: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        // Ajouter la requête à la file d'attente
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}