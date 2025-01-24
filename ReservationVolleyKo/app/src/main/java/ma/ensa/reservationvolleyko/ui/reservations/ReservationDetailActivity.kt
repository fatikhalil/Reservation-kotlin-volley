package ma.ensa.reservationvolleyko.ui.reservations

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import ma.ensa.reservationvolleyko.R
import ma.ensa.reservationvolleyko.models.Chambre
import ma.ensa.reservationvolleyko.models.Client
import ma.ensa.reservationvolleyko.models.DispoChambre
import ma.ensa.reservationvolleyko.models.ReservationInput
import ma.ensa.reservationvolleyko.models.TypeChambre
import org.json.JSONException
import org.json.JSONObject

class ReservationDetailActivity : AppCompatActivity() {

    private lateinit var textViewClientId: TextView
    private lateinit var textViewChambreId: TextView
    private lateinit var textViewDateDebut: TextView
    private lateinit var textViewDateFin: TextView
    private lateinit var textViewPreferences: TextView
    private lateinit var buttonEditReservation: Button
    private lateinit var buttonDeleteReservation: Button
    private var reservationId: Long = -1
    private lateinit var requestQueue: RequestQueue // File d'attente des requêtes Volley

    // URL de base de l'API
    companion object {
        private const val BASE_URL = "http://192.168.1.160:8082/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_detail)

        textViewClientId = findViewById(R.id.textViewClientId)
        textViewChambreId = findViewById(R.id.textViewChambreId)
        textViewDateDebut = findViewById(R.id.textViewDateDebut)
        textViewDateFin = findViewById(R.id.textViewDateFin)
        textViewPreferences = findViewById(R.id.textViewPreferences)
        buttonEditReservation = findViewById(R.id.buttonEditReservation)
        buttonDeleteReservation = findViewById(R.id.buttonDeleteReservation)

        // Initialiser la file d'attente Volley
        requestQueue = Volley.newRequestQueue(this)

        // Récupérer l'ID de la réservation depuis l'intent
        reservationId = intent.getLongExtra("reservationId", -1)

        if (reservationId != -1L) {
            loadReservationDetails(reservationId)
        }

        // Gérer le clic sur le bouton "Modifier Réservation"
        buttonEditReservation.setOnClickListener {
            val intent = Intent(this@ReservationDetailActivity, AddReservationActivity::class.java)
            intent.putExtra("reservationId", reservationId)
            startActivity(intent)
        }

        // Gérer le clic sur le bouton "Supprimer Réservation"
        buttonDeleteReservation.setOnClickListener { deleteReservation(reservationId) }
    }

    /**
     * Charge les détails d'une réservation depuis l'API.
     */
    private fun loadReservationDetails(reservationId: Long) {
        val url = BASE_URL + "api/reservations/$reservationId"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    // Extraire les données de la réponse JSON
                    val reservationInput = ReservationInput()

                    // Extraire les informations du client
                    val clientJson = response.getJSONObject("client")
                    val client = Client().apply { id = clientJson.getLong("id") }
                    reservationInput.client = client

                    // Extraire les informations de la chambre
                    val chambreJson = response.getJSONObject("chambre")
                    val chambre = Chambre().apply {
                        id = chambreJson.getLong("id")
                        typeChambre = TypeChambre.valueOf(chambreJson.getString("typeChambre"))
                        prix = chambreJson.getDouble("prix")
                        dispoChambre = DispoChambre.valueOf(chambreJson.getString("dispoChambre"))
                    }
                    reservationInput.chambre = chambre

                    // Extraire les autres informations
                    reservationInput.dateDebut = response.getString("dateDebut")
                    reservationInput.dateFin = response.getString("dateFin")
                    reservationInput.preferences = response.getString("preferences")

                    // Afficher les détails de la réservation
                    textViewClientId.text = "Client ID: ${client.id}"
                    textViewChambreId.text = "Chambre ID: ${chambre.id}"
                    textViewDateDebut.text = "Date Début: ${reservationInput.dateDebut}"
                    textViewDateFin.text = "Date Fin: ${reservationInput.dateFin}"
                    textViewPreferences.text = "Préférences: ${reservationInput.preferences}"
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this@ReservationDetailActivity, "Failed to parse reservation details", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this@ReservationDetailActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(request)
    }

    /**
     * Supprime une réservation existante.
     */
    private fun deleteReservation(reservationId: Long) {
        val url = BASE_URL + "api/reservations/$reservationId"

        val request = StringRequest(
            Request.Method.DELETE, url,
            { response ->
                Toast.makeText(this@ReservationDetailActivity, "Reservation deleted successfully", Toast.LENGTH_SHORT).show()
                finish() // Fermer l'activité après la suppression
            },
            { error ->
                Toast.makeText(this@ReservationDetailActivity, "Failed to delete reservation", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(request)
    }
}