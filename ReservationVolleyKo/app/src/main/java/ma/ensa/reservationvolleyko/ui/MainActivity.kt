package ma.ensa.reservationvolleyko.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ma.ensa.reservationvolleyko.R
import ma.ensa.reservationvolleyko.ui.chambres.ChambreListActivity
import ma.ensa.reservationvolleyko.ui.reservations.ReservationListActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bouton pour accéder à la gestion des chambres
        val btnGestionChambres: Button = findViewById(R.id.btnRoomManagement)
        btnGestionChambres.setOnClickListener {
            val intent = Intent(this@MainActivity, ChambreListActivity::class.java)
            startActivity(intent)
        }

        // Bouton pour accéder à la gestion des réservations
        val btnGestionReservations: Button = findViewById(R.id.btnReservationManagement)
        btnGestionReservations.setOnClickListener {
            val intent = Intent(this@MainActivity, ReservationListActivity::class.java)
            startActivity(intent)
        }
    }
}