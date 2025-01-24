package ma.ensa.reservationvolleyko.ui.auth

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
import org.json.JSONObject

class SignupActivity : AppCompatActivity() {

    private lateinit var editTextNom: EditText
    private lateinit var editTextPrenom: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextTelephone: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialiser les vues
        editTextNom = findViewById(R.id.editTextLastName)
        editTextPrenom = findViewById(R.id.editTextFirstName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextTelephone = findViewById(R.id.editTextPhone)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignup = findViewById(R.id.buttonSignup)

        // Gérer le clic sur le bouton d'inscription
        buttonSignup.setOnClickListener { signupUser() }
    }

    private fun signupUser() {
        // Récupérer les valeurs saisies par l'utilisateur
        val nom = editTextNom.text.toString().trim()
        val prenom = editTextPrenom.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val telephone = editTextTelephone.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        // Vérifier que les champs ne sont pas vides
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telephone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        // Créer un objet AuthRequest
        val authRequest = AuthRequest().apply {
            this.nom = nom
            this.prenom = prenom
            this.email = email
            this.telephone = telephone
            this.password = password
        }

        // Convertir l'objet AuthRequest en JSON
        val jsonBody = JSONObject().apply {
            put("nom", authRequest.nom)
            put("prenom", authRequest.prenom)
            put("email", authRequest.email)
            put("telephone", authRequest.telephone)
            put("password", authRequest.password)
        }

        // URL de l'endpoint d'inscription
        val url = "http://192.168.1.160:8082/api/auth/signup"

        // Créer une requête POST avec Volley
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            Response.Listener<JSONObject> { response ->
                try {
                    // Désérialiser la réponse JSON en AuthResponse
                    val gson = Gson()
                    val authResponse = gson.fromJson(response.toString(), AuthResponse::class.java)

                    // Afficher un message de succès
                    Toast.makeText(this@SignupActivity, "Inscription réussie", Toast.LENGTH_SHORT).show()

                    // Retour à l'écran de connexion
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@SignupActivity, "Erreur lors de la désérialisation", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Gérer l'erreur
                Toast.makeText(this@SignupActivity, "Erreur: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        // Ajouter la requête à la file d'attente
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}