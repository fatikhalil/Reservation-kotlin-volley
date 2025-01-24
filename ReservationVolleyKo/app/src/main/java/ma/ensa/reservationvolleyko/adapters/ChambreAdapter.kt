package ma.ensa.reservationvolleyko.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import ma.ensa.reservationvolleyko.R
import ma.ensa.reservationvolleyko.models.Chambre

class ChambreAdapter(
    private var chambres: List<Chambre>,
    private val context: Context,
    private val onChambreListener: OnChambreListener
) : RecyclerView.Adapter<ChambreAdapter.ChambreViewHolder>() {

    // Interface pour gérer les clics sur les éléments de la liste
    interface OnChambreListener {
        fun onChambreClick(position: Int)
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ChambreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chambre, parent, false)
        return ChambreViewHolder(view, onChambreListener)
    }

    override fun onBindViewHolder(@NonNull holder: ChambreViewHolder, position: Int) {
        val chambre = chambres[position]
        holder.typeChambre.text = chambre.typeChambre.toString()
        holder.prix.text = chambre.prix.toString()
        holder.dispoChambre.text = chambre.dispoChambre.toString()
    }

    override fun getItemCount(): Int {
        return chambres.size
    }

    // Méthode pour mettre à jour la liste des chambres
    fun updateChambres(newChambres: List<Chambre>) {
        this.chambres = newChambres
        notifyDataSetChanged() // Notifier l'adaptateur que les données ont changé
    }

    // Méthode pour récupérer une chambre à une position donnée
    fun getChambreAt(position: Int): Chambre {
        return chambres[position]
    }

    // ViewHolder avec gestion des clics
    class ChambreViewHolder(
        itemView: View,
        private val onChambreListener: OnChambreListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val typeChambre: TextView = itemView.findViewById(R.id.typeChambre)
        val prix: TextView = itemView.findViewById(R.id.prix)
        val dispoChambre: TextView = itemView.findViewById(R.id.dispoChambre)

        init {
            itemView.setOnClickListener(this) // Définir le clic sur l'élément
        }

        override fun onClick(v: View?) {
            onChambreListener.onChambreClick(adapterPosition) // Notifier le listener
        }
    }
}