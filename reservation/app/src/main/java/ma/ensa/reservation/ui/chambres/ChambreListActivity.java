package ma.ensa.reservation.ui.chambres;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ma.ensa.reservation.R;
import ma.ensa.reservation.adapters.ChambreAdapter;
import ma.ensa.reservation.api.ApiClient;
import ma.ensa.reservation.api.ApiInterface;
import ma.ensa.reservation.models.Chambre;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChambreListActivity extends AppCompatActivity implements ChambreAdapter.OnChambreListener {

    private RecyclerView recyclerView;
    private ChambreAdapter adapter;
    private Button btnAddChambre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chambre_list);

        // Initialiser les vues
        recyclerView = findViewById(R.id.recyclerView);
        btnAddChambre = findViewById(R.id.btnAddChambre);

        // Configurer le RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configurer le bouton "Ajouter une chambre"
        btnAddChambre.setOnClickListener(v -> {
            Intent intent = new Intent(ChambreListActivity.this, AddChambreActivity.class);
            startActivityForResult(intent, 1); // Code de requête 1 pour l'ajout
        });

        // Charger les chambres depuis l'API
        loadChambres();
    }

    /**
     * Charge la liste des chambres depuis l'API.
     */
    private void loadChambres() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Chambre>> call = apiService.getAllChambres();
        call.enqueue(new Callback<List<Chambre>>() {
            @Override
            public void onResponse(Call<List<Chambre>> call, Response<List<Chambre>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Chambre> chambres = response.body();

                    // Initialiser l'adaptateur avec la liste des chambres
                    adapter = new ChambreAdapter(chambres, ChambreListActivity.this, ChambreListActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    // Gérer le cas où la réponse est vide ou non réussie
                    Toast.makeText(ChambreListActivity.this, "Erreur lors du chargement des chambres", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Chambre>> call, Throwable t) {
                // Gérer l'erreur de connexion
                Toast.makeText(ChambreListActivity.this, "Erreur de connexion : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Recharger les chambres après une modification ou un ajout
        if ((requestCode == 1 || requestCode == 2) && resultCode == RESULT_OK) {
            loadChambres();
        }
    }

    @Override
    public void onChambreClick(int position) {
        // Récupérer la chambre sélectionnée
        Chambre chambre = adapter.getChambreAt(position);

        // Ouvrir l'activité de détail de la chambre
        Intent intent = new Intent(ChambreListActivity.this, ChambreDetailActivity.class);
        intent.putExtra("CHAMBRE_ID", chambre.getId());
        startActivityForResult(intent, 2); // Code de requête 2 pour la modification
    }
}