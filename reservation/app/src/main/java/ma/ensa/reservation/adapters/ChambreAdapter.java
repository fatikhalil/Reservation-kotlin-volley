package ma.ensa.reservation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ma.ensa.reservation.R;
import ma.ensa.reservation.models.Chambre;

public class ChambreAdapter extends RecyclerView.Adapter<ChambreAdapter.ChambreViewHolder> {

    private List<Chambre> chambres;
    private Context context;
    private OnChambreListener onChambreListener;

    // Interface pour gérer les clics sur les éléments de la liste
    public interface OnChambreListener {
        void onChambreClick(int position);
    }

    // Constructeur avec OnChambreListener
    public ChambreAdapter(List<Chambre> chambres, Context context, OnChambreListener onChambreListener) {
        this.chambres = chambres;
        this.context = context;
        this.onChambreListener = onChambreListener;
    }

    @NonNull
    @Override
    public ChambreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chambre, parent, false);
        return new ChambreViewHolder(view, onChambreListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChambreViewHolder holder, int position) {
        Chambre chambre = chambres.get(position);
        holder.typeChambre.setText(chambre.getTypeChambre().toString());
        holder.prix.setText(String.valueOf(chambre.getPrix()));
        holder.dispoChambre.setText(chambre.getDispoChambre().toString());
    }

    @Override
    public int getItemCount() {
        return chambres.size();
    }

    // Méthode pour mettre à jour la liste des chambres
    public void updateChambres(List<Chambre> newChambres) {
        this.chambres = newChambres;
        notifyDataSetChanged(); // Notifier l'adaptateur que les données ont changé
    }

    // Méthode pour récupérer une chambre à une position donnée
    public Chambre getChambreAt(int position) {
        return chambres.get(position);
    }

    // ViewHolder avec gestion des clics
    static class ChambreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView typeChambre, prix, dispoChambre;
        OnChambreListener onChambreListener;

        public ChambreViewHolder(@NonNull View itemView, OnChambreListener onChambreListener) {
            super(itemView);
            typeChambre = itemView.findViewById(R.id.typeChambre);
            prix = itemView.findViewById(R.id.prix);
            dispoChambre = itemView.findViewById(R.id.dispoChambre);

            this.onChambreListener = onChambreListener;
            itemView.setOnClickListener(this); // Définir le clic sur l'élément
        }

        @Override
        public void onClick(View v) {
            onChambreListener.onChambreClick(getAdapterPosition()); // Notifier le listener
        }
    }
}