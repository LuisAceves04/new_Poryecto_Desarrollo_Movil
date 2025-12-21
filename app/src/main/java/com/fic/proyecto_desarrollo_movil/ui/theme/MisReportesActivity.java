package com.fic.proyecto_desarrollo_movil.ui.theme;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fic.proyecto_desarrollo_movil.Adapters.ReporteAdapter;
import com.fic.proyecto_desarrollo_movil.Modelos.Reporte;
import com.fic.proyecto_desarrollo_movil.R;
import com.fic.proyecto_desarrollo_movil.ui.theme.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MisReportesActivity extends AppCompatActivity {
    RecyclerView rv;
    ReporteAdapter adapter;
    List<Reporte> listaReportes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_reportes);

        rv = findViewById(R.id.rvMisReportes);
        rv.setLayoutManager(new LinearLayoutManager(this));

        cargarReportes();
    }

    private void cargarReportes() {
        // 1. CORRECCIÓN: Usar el mismo nombre que en HomeActivity ("user_session")
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        int userId = prefs.getInt("USER_ID", 0);

        // Si prefieres probar con datos reales ahora mismo antes de arreglar el Login,
        // puedes descomentar la siguiente línea para forzar el ID 1:
        // userId = 1;

        String url = "http://10.0.2.2/parkmanager/api/reportes/listar_reportes.php?id_usuario=" + userId;

        new Thread(() -> {
            String respuesta = NetworkUtils.get(url);
            runOnUiThread(() -> {
                if (respuesta == null) return; // Evitar errores si el servidor no responde

                try {
                    // Limpiamos la lista por si se recarga la pantalla
                    listaReportes.clear();

                    JSONArray jsonArray = new JSONArray(respuesta);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        listaReportes.add(new Reporte(
                                obj.getString("nombre_parque"),
                                obj.getString("descripcion"),
                                obj.getString("fecha_creacion")
                        ));
                    }

                    // 2. MEJORA: Solo crear el adapter si no existe, o notificar cambios
                    if (adapter == null) {
                        adapter = new ReporteAdapter(listaReportes);
                        rv.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    // 3. EXPLICACIÓN: Aquí caía el error antes porque recibías un {} en vez de []
                    e.printStackTrace();
                }
            });
        }).start();
    }
}