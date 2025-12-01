package proyecto_desarrollo_movil;

import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.fic.proyecto_desarrollo_movil.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tvParques;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvParques = findViewById(R.id.tvParques);

        List<Parques> listaParques;

        AppDatabase appDatabase = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "dbPruebas"
        ).allowMainThreadQueries().build();

        DaoParque daoParque = appDatabase.daoParque();

        daoParque.insertarParque(new Parques("P-001", "Revolucion", "Álvaro Obregón 184 South Center CP 80000, 667 712 7501 Culiacán Rosales, Sin., Mexico"));
        daoParque.insertarParque(new Parques("P-002", "Riveras", "Blvd Francisco Labastida Ochoa S/N, Desarrollo Urbano Tres Ríos, C.P. 80230, Culiacán, Sinaloa"));

        listaParques = daoParque.obtenerParques();

        String texto = "Lista de Parques:\n";

        if (listaParques != null && !listaParques.isEmpty()) {
            for (int i = 0; i < listaParques.size(); i++){
                Parques parque = listaParques.get(i);
                texto += "Parque " + (i + 1) + ": " + parque.idParque + ", " + parque.Nombre + ", " + parque.Direccion + "\n";
            }
            tvParques.setText(texto);
            Log.d(TAG, "Parques mostrados correctamente.");

        } else {
            tvParques.setText("No se encontraron parques.");
            Log.w(TAG, "Lista de parques vacía o nula.");
        }
    }
}