package ma.ensa.reservationvolleyko.ui.reservations

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ma.ensa.reservationvolleyko.R
import ma.ensa.reservationvolleyko.adapters.ReservationAdapter
import ma.ensa.reservationvolleyko.api.VolleySingleton
import ma.ensa.reservationvolleyko.models.ReservationInput
import org.json.JSONArray

class ReservationListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReservationAdapter
    private lateinit var buttonAddReservation: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_list)

        recyclerView = findViewById(R.id.reservation_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        buttonAddReservation = findViewById(R.id.buttonAddReservation)
        buttonAddReservation.setOnClickListener {
            val intent = Intent(this@ReservationListActivity, AddReservationActivity::class.java)
            startActivity(intent)
        }

        loadReservations()
    }

    override fun onResume() {
        super.onResume()
        loadReservations() // Recharger les réservations à chaque retour à l'activité
    }

    private fun loadReservations() {
        val url = "http://192.168.1.160:8082/api/reservations" // URL de l'endpoint

        // Temps de début
        val startTime = System.currentTimeMillis()

        // Créer une requête GET avec Volley
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                // Temps de fin
                val endTime = System.currentTimeMillis()
                val durationMs = endTime - startTime // Temps écoulé en millisecondes

                // Afficher le temps de réponse dans les logs
                println("Temps de réponse pour loadReservations : $durationMs ms")

                try {
                    // Désérialiser la réponse JSON en une liste de réservations
                    val gson = Gson()
                    val reservations: List<ReservationInput> = gson.fromJson(
                        response.toString(),
                        object : TypeToken<List<ReservationInput>>() {}.type
                    )

                    // Mettre à jour l'adaptateur
                    adapter = ReservationAdapter(reservations, this@ReservationListActivity)
                    recyclerView.adapter = adapter
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@ReservationListActivity,
                        "Erreur lors de la lecture des réservations",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            Response.ErrorListener { error ->
                // Temps de fin en cas d'échec
                val endTime = System.currentTimeMillis()
                val durationMs = endTime - startTime // Temps écoulé en millisecondes
                // Gérer l'erreur
                Toast.makeText(
                    this@ReservationListActivity,
                    "Erreur de connexion",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        // Ajouter la requête à la file d'attente
        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest)
    }
}