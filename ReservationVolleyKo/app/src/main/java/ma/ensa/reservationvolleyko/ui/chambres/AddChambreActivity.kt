package ma.ensa.reservationvolleyko.ui.chambres

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import ma.ensa.reservationvolleyko.R
import ma.ensa.reservationvolleyko.api.VolleySingleton
import ma.ensa.reservationvolleyko.models.Chambre
import ma.ensa.reservationvolleyko.models.DispoChambre
import ma.ensa.reservationvolleyko.models.TypeChambre
import org.json.JSONObject

class AddChambreActivity : AppCompatActivity() {

    private lateinit var spinnerTypeChambre: Spinner
    private lateinit var spinnerDispoChambre: Spinner
    private lateinit var editPrix: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_chambre)

        // Initialisation des vues
        spinnerTypeChambre = findViewById(R.id.spinnerTypeChambre)
        editPrix = findViewById(R.id.editPrix)
        spinnerDispoChambre = findViewById(R.id.spinnerDispoChambre)
        btnSave = findViewById(R.id.btnUpdate)

        // Configurer les Spinners
        setupSpinners()

        // Bouton de sauvegarde
        btnSave.setOnClickListener { saveChambre() }
    }

    private fun setupSpinners() {
        // Adapter pour TypeChambre
        val typeChambreAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, TypeChambre.values()
        )
        typeChambreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTypeChambre.adapter = typeChambreAdapter

        // Adapter pour DispoChambre
        val dispoChambreAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, DispoChambre.values()
        )
        dispoChambreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDispoChambre.adapter = dispoChambreAdapter
    }

    private fun saveChambre() {
        val prixStr = editPrix.text.toString()

        // Validation des champs
        if (prixStr.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Créer une nouvelle chambre
            val chambre = Chambre().apply {
                typeChambre = spinnerTypeChambre.selectedItem as TypeChambre
                prix = prixStr.toDouble()
                dispoChambre = spinnerDispoChambre.selectedItem as DispoChambre
            }

            // Convertir l'objet Chambre en JSON
            val gson = Gson()
            val jsonBody = gson.toJson(chambre)

            // URL de l'endpoint pour ajouter une chambre
            val url = "http://192.168.1.160:8082/api/chambres"

            // Temps de début
            val startTime = System.currentTimeMillis()

            // Créer une requête POST avec Volley
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, JSONObject(jsonBody),
                Response.Listener<JSONObject> { response ->
                    // Temps de fin
                    val endTime = System.currentTimeMillis()
                    val durationMs = endTime - startTime // Temps écoulé en millisecondes

                    // Mesurer la taille des données reçues
                    val jsonString = response.toString()
                    val sizeInBytes = jsonString.toByteArray().size // Taille en octets
                    val sizeInKB = sizeInBytes / 1024.0 // Convertir en KB

                    // Afficher les résultats dans les logs
                    println("Taille des données reçues (POST) : $sizeInKB KB")
                    println("Temps de réponse (POST) : $durationMs ms")

                    // Gérer la réponse JSON
                    Toast.makeText(this@AddChambreActivity, "Chambre ajoutée", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish() // Fermer l'activité après l'ajout
                },
                Response.ErrorListener { error ->
                    // Temps de fin en cas d'échec
                    val endTime = System.currentTimeMillis()
                    val durationMs = endTime - startTime // Temps écoulé en millisecondes

                    // Afficher les résultats dans les logs
                    println("Temps de réponse (POST - échec) : $durationMs ms")

                    // Gérer l'erreur
                    Toast.makeText(
                        this@AddChambreActivity,
                        "Erreur d'ajout: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

            // Ajouter la requête à la file d'attente
            VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Prix invalide", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Erreur lors de la création de la chambre", Toast.LENGTH_SHORT).show()
        }
    }
}