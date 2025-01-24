package ma.ensa.reservationvolleyko.ui.chambres

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
import ma.ensa.reservationvolleyko.adapters.ChambreAdapter
import ma.ensa.reservationvolleyko.api.VolleySingleton
import ma.ensa.reservationvolleyko.models.Chambre
import org.json.JSONArray

class ChambreListActivity : AppCompatActivity(), ChambreAdapter.OnChambreListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChambreAdapter
    private lateinit var btnAddChambre: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chambre_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAddChambre = findViewById(R.id.btnAddChambre)
        btnAddChambre.setOnClickListener {
            val intent = Intent(this@ChambreListActivity, AddChambreActivity::class.java)
            startActivityForResult(intent, 1)
        }

        loadChambres()
    }
    private fun loadChambres() {
        val url = "http://192.168.1.160:8082/api/chambres" // URL de l'endpoint

        // Temps de début
        val startTime = System.currentTimeMillis()

        // Créer une requête GET avec Volley
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                // Temps de fin
                val endTime = System.currentTimeMillis()
                val durationMs = endTime - startTime // Temps écoulé en millisecondes

                try {
                    // Désérialiser la réponse JSON en une liste de chambres
                    val gson = Gson()
                    val chambres: List<Chambre> = gson.fromJson(response.toString(), object : TypeToken<List<Chambre>>() {}.type)

                    // Mesurer la taille des données reçues
                    val jsonString = response.toString()
                    val sizeInBytes = jsonString.toByteArray().size // Taille en octets
                    val sizeInKB = sizeInBytes / 1024.0 // Convertir en KB

                    // Afficher les résultats dans les logs
                    println("Taille des données reçues (GET) : $sizeInKB KB")
                    println("Temps de réponse (GET) : $durationMs ms")

                    // Mettre à jour l'adaptateur
                    adapter = ChambreAdapter(chambres, this@ChambreListActivity, this@ChambreListActivity)
                    recyclerView.adapter = adapter
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@ChambreListActivity, "Erreur lors de la désérialisation", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Temps de fin en cas d'échec
                val endTime = System.currentTimeMillis()
                val durationMs = endTime - startTime // Temps écoulé en millisecondes

                // Afficher les résultats dans les logs
                println("Temps de réponse (GET - échec) : $durationMs ms")

                // Gérer l'erreur
                Toast.makeText(this@ChambreListActivity, "Erreur: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        // Ajouter la requête à la file d'attente
        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 1 || requestCode == 2) && resultCode == RESULT_OK) {
            loadChambres() // Recharger les chambres après une modification ou un ajout
        }
    }

    override fun onChambreClick(position: Int) {
        val chambre = adapter.getChambreAt(position) // Utiliser la méthode getChambreAt
        val intent = Intent(this@ChambreListActivity, ChambreDetailActivity::class.java)
        intent.putExtra("CHAMBRE_ID", chambre.id)
        startActivityForResult(intent, 2)
    }
}