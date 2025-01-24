package ma.ensa.reservation.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ma.ensa.reservation.R;
import ma.ensa.reservation.models.Chambre;
import ma.ensa.reservation.models.Client;
import ma.ensa.reservation.models.ReservationInput;
import ma.ensa.reservation.ui.reservations.ReservationDetailActivity;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private List<ReservationInput> reservationList;
    private Context context;

    public ReservationAdapter(List<ReservationInput> reservationList, Context context) {
        this.reservationList = reservationList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        ReservationInput reservationInput = reservationList.get(position);

        // Afficher les détails du client
        Client client = reservationInput.getClient();
        if (client != null) {
            holder.textViewClientId.setText("Client: " + client.getNom() + " " + client.getPrenom());
        } else {
            holder.textViewClientId.setText("Client: Non disponible");
        }

        // Afficher les détails de la chambre
        Chambre chambre = reservationInput.getChambre();
        if (chambre != null) {
            holder.textViewChambreId.setText("Chambre: " + chambre.getTypeChambre() + " - " + chambre.getPrix() + "€");
        } else {
            holder.textViewChambreId.setText("Chambre: Non disponible");
        }

        // Afficher les autres détails de la réservation
        holder.textViewDateDebut.setText("Date Début: " + reservationInput.getDateDebut());
        holder.textViewDateFin.setText("Date Fin: " + reservationInput.getDateFin());
        holder.textViewPreferences.setText("Préférences: " + reservationInput.getPreferences());

        // Ajouter un écouteur de clics sur l'élément
        holder.itemView.setOnClickListener(v -> {
            // Log pour vérifier que l'écouteur de clics est déclenché
            Log.d("ReservationAdapter", "Item clicked, reservation ID: " + reservationInput.getId());

            // Naviguer vers ReservationDetailActivity avec l'ID de la réservation
            Intent intent = new Intent(context, ReservationDetailActivity.class);
            intent.putExtra("reservationId", reservationInput.getId()); // Passer l'ID de la réservation
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewClientId, textViewChambreId, textViewDateDebut, textViewDateFin, textViewPreferences;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewClientId = itemView.findViewById(R.id.textViewClientId);
            textViewChambreId = itemView.findViewById(R.id.textViewChambreId);
            textViewDateDebut = itemView.findViewById(R.id.textViewDateDebut);
            textViewDateFin = itemView.findViewById(R.id.textViewDateFin);
            textViewPreferences = itemView.findViewById(R.id.textViewPreferences);
        }
    }
}