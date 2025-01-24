package ma.ensa.reservationvolleyko.ui.reservations

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import ma.ensa.reservationvolleyko.R
import ma.ensa.reservationvolleyko.api.VolleySingleton
import ma.ensa.reservationvolleyko.models.Chambre
import ma.ensa.reservationvolleyko.models.Client
import ma.ensa.reservationvolleyko.models.DispoChambre
import ma.ensa.reservationvolleyko.models.ReservationInput
import ma.ensa.reservationvolleyko.models.TypeChambre
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddReservationActivity : AppCompatActivity() {

    private lateinit var editTextClientId: EditText
    private lateinit var editTextDateDebut: EditText
    private lateinit var editTextDateFin: EditText
    private lateinit var editTextPreferences: EditText
    private lateinit var spinnerChambres: Spinner
    private lateinit var buttonSaveReservation: Button
    private var reservationId: Long = -1 // Pour stocker l'ID de la réservation en mode édition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reservation)

        // Initialiser les vues
        editTextClientId = findViewById(R.id.editClientId)
        editTextDateDebut = findViewById(R.id.editDateDebut)
        editTextDateFin = findViewById(R.id.editDateFin)
        editTextPreferences = findViewById(R.id.editPreferences)
        spinnerChambres = findViewById(R.id.spinnerChambre)
        buttonSaveReservation = findViewById(R.id.btnSaveReservation)

        // Récupérer l'ID de la réservation depuis l'intent (si en mode édition)
        reservationId = intent.getLongExtra("reservationId", -1)

        // Charger les chambres disponibles
        loadAvailableChambres()

        // Si en mode édition, charger les détails de la réservation
        if (reservationId != -1L) {
            loadReservationDetails(reservationId)
        }

        // Gérer le clic sur le bouton "Sauvegarder Réservation"
        buttonSaveReservation.setOnClickListener {
            if (reservationId != -1L) {
                updateReservation(reservationId) // Mode édition
            } else {
                saveReservation() // Mode création
            }
        }
    }

    /**
     * Charge les chambres disponibles depuis l'API et les affiche dans le Spinner.
     */
    private fun loadAvailableChambres() {
        val url = "http://192.168.1.160:8082/api/chambres/available"

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val chambres = mutableListOf<Chambre>()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)

                        // Parsing de la chambre
                        val chambre = Chambre().apply {
                            id = jsonObject.getLong("id")
                            typeChambre = TypeChambre.valueOf(jsonObject.getString("typeChambre"))
                            dispoChambre = DispoChambre.valueOf(jsonObject.getString("dispoChambre"))
                            prix = jsonObject.getDouble("prix")
                        }
                        chambres.add(chambre)
                    }

                    // Afficher les chambres dans le Spinner
                    val adapter = ArrayAdapter(
                        this@AddReservationActivity,
                        android.R.layout.simple_spinner_item,
                        chambres
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerChambres.adapter = adapter
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this@AddReservationActivity, "Failed to parse chambres", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this@AddReservationActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }

    /**
     * Charge les détails d'une réservation existante pour les afficher dans le formulaire.
     */
    private fun loadReservationDetails(reservationId: Long) {
        val url = "http://192.168.1.160:8082/api/reservations/$reservationId"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val reservationInput = ReservationInput().apply {
                        dateDebut = response.getString("dateDebut")
                        dateFin = response.getString("dateFin")
                        preferences = response.getString("preferences")

                        // Parsing du client
                        val clientJson = response.getJSONObject("client")
                        client = Client().apply {
                            id = clientJson.getLong("id")
                        }

                        // Parsing de la chambre
                        val chambreJson = response.getJSONObject("chambre")
                        chambre = Chambre().apply {
                            id = chambreJson.getLong("id")
                            typeChambre = TypeChambre.valueOf(chambreJson.getString("typeChambre"))
                            dispoChambre = DispoChambre.valueOf(chambreJson.getString("dispoChambre"))
                            prix = chambreJson.getDouble("prix")
                        }
                    }

                    // Remplir les champs du formulaire
                    editTextClientId.setText(reservationInput.client?.id.toString())
                    editTextDateDebut.setText(reservationInput.dateDebut)
                    editTextDateFin.setText(reservationInput.dateFin)
                    editTextPreferences.setText(reservationInput.preferences)

                    // Sélectionner la chambre dans le Spinner
                    for (i in 0 until spinnerChambres.count) {
                        val chambreSpinner = spinnerChambres.getItemAtPosition(i) as Chambre
                        if (chambreSpinner.id == reservationInput.chambre?.id) {
                            spinnerChambres.setSelection(i)
                            break
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this@AddReservationActivity, "Failed to parse reservation details", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this@AddReservationActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        )

        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }

    /**
     * Sauvegarde une nouvelle réservation en utilisant les données saisies par l'utilisateur.
     */
    private fun saveReservation() {
        // Validation des champs
        if (editTextClientId.text.toString().isEmpty() ||
            editTextDateDebut.text.toString().isEmpty() ||
            editTextDateFin.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidDate(editTextDateDebut.text.toString()) || !isValidDate(editTextDateFin.text.toString())) {
            Toast.makeText(this, "Format de date invalide. Utilisez yyyy-MM-dd", Toast.LENGTH_SHORT).show()
            return
        }

        // Récupération des données
        val clientId = editTextClientId.text.toString().toLong()
        val selectedChambre = spinnerChambres.selectedItem as? Chambre

        if (selectedChambre == null) {
            Toast.makeText(this, "Veuillez sélectionner une chambre", Toast.LENGTH_SHORT).show()
            return
        }

        // Création des objets
        val client = Client().apply { id = clientId }
        val dateDebut = editTextDateDebut.text.toString()
        val dateFin = editTextDateFin.text.toString()
        val preferences = editTextPreferences.text.toString()

        // Création de l'objet ReservationInput
        val reservationInput = ReservationInput(client, selectedChambre, dateDebut, dateFin, preferences)

        // Envoi de la requête
        val url = "http://192.168.1.160:8082/api/reservations"
        val gson = Gson()
        val json = gson.toJson(reservationInput)

        val request = object : JsonObjectRequest(Request.Method.POST, url, null,
            { response ->
                Toast.makeText(this@AddReservationActivity, "Reservation saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            },
            { error ->
                val errorMessage = "Failed to save reservation: " +
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String(error.networkResponse.data, Charsets.UTF_8)
                            } catch (e: Exception) {
                                "Unknown error"
                            }
                        } else {
                            "No error body"
                        }
                Toast.makeText(this@AddReservationActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getBody(): ByteArray {
                return json.toByteArray()
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }

        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }

    /**
     * Valide le format de la date (yyyy-MM-dd).
     */
    private fun isValidDate(date: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(date)
            true
        } catch (e: ParseException) {
            false
        }
    }

    /**
     * Met à jour une réservation existante en utilisant les données saisies par l'utilisateur.
     */
    private fun updateReservation(reservationId: Long) {
        // Validation des champs
        if (editTextClientId.text.toString().isEmpty() ||
            editTextDateDebut.text.toString().isEmpty() ||
            editTextDateFin.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidDate(editTextDateDebut.text.toString()) || !isValidDate(editTextDateFin.text.toString())) {
            Toast.makeText(this, "Format de date invalide. Utilisez yyyy-MM-dd", Toast.LENGTH_SHORT).show()
            return
        }

        // Récupération des données
        val clientId = editTextClientId.text.toString().toLong()
        val selectedChambre = spinnerChambres.selectedItem as? Chambre

        if (selectedChambre == null) {
            Toast.makeText(this, "Veuillez sélectionner une chambre", Toast.LENGTH_SHORT).show()
            return
        }

        // Création des objets
        val client = Client().apply { id = clientId }
        val dateDebut = editTextDateDebut.text.toString()
        val dateFin = editTextDateFin.text.toString()
        val preferences = editTextPreferences.text.toString()

        // Création de l'objet ReservationInput
        val reservationInput = ReservationInput(client, selectedChambre, dateDebut, dateFin, preferences)

        // Envoi de la requête PUT pour mettre à jour la réservation
        val url = "http://192.168.1.160:8082/api/reservations/$reservationId"
        val gson = Gson()
        val json = gson.toJson(reservationInput)

        val request = object : JsonObjectRequest(Request.Method.PUT, url, null,
            { response ->
                Toast.makeText(this@AddReservationActivity, "Reservation updated successfully", Toast.LENGTH_SHORT).show()
                finish() // Fermer l'activité après la mise à jour
            },
            { error ->
                val errorMessage = "Failed to update reservation: " +
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String(error.networkResponse.data, Charsets.UTF_8)
                            } catch (e: Exception) {
                                "Unknown error"
                            }
                        } else {
                            "No error body"
                        }
                Toast.makeText(this@AddReservationActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getBody(): ByteArray {
                return json.toByteArray()
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }

        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }
}