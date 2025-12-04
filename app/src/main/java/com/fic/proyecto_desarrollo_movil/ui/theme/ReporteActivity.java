package com.fic.proyecto_desarrollo_movil.ui.theme;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import com.fic.proyecto_desarrollo_movil.R;

public class ReporteActivity extends AppCompatActivity {

    private TextView tvNombreParque;
    private EditText etDescripcionReporte;
    private Button btnEnviarReporte;
    private String nombreParqueSeleccionado;

    private static final String TAG = "ReporteActivity";
    private static final String API_ENDPOINT = "reportes/crear.php";
    private static final String PREFS_NAME = "user_session";
    private static final String USER_ID_KEY = "USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_reporte);

        tvNombreParque = findViewById(R.id.tvNombreParqueSeleccionado);
        etDescripcionReporte = findViewById(R.id.etDescripcionReporte);
        btnEnviarReporte = findViewById(R.id.btnEnviarReporte);

        if (getIntent().hasExtra("PARQUE_SELECCIONADO")) {
            nombreParqueSeleccionado = getIntent().getStringExtra("PARQUE_SELECCIONADO");
            tvNombreParque.setText(nombreParqueSeleccionado);
        } else {
            nombreParqueSeleccionado = "";
            tvNombreParque.setText("ERROR: No se seleccionó parque.");
            btnEnviarReporte.setEnabled(false);
            Toast.makeText(this, "Debe seleccionar un parque.", Toast.LENGTH_LONG).show();
        }

        btnEnviarReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarReporte();
            }
        });
    }

    private int getUserId() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getInt(USER_ID_KEY, 0);
    }

    private void enviarReporte() {
        int idUsuario = getUserId();
        String descripcion = etDescripcionReporte.getText().toString().trim();

        if (idUsuario == 0) {
            Toast.makeText(this, "Error de sesión: ID de usuario no encontrado.", Toast.LENGTH_SHORT).show();
            // Opcional: Redirigir al Login
            return;
        }

        if (descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor, escribe los detalles del reporte.", Toast.LENGTH_SHORT).show();
            return;
        }

        btnEnviarReporte.setEnabled(false);
        btnEnviarReporte.setText("Enviando...");

        new Thread(new Runnable() {
            @Override
            public void run() {

                JSONObject jsonObject = new JSONObject();
                try {
                    // El PHP espera 'id_usuario'
                    jsonObject.put("id_usuario", idUsuario);
                    jsonObject.put("nombre_parque", nombreParqueSeleccionado);
                    jsonObject.put("descripcion", descripcion);
                } catch (JSONException e) {
                    Log.e(TAG, "Error al crear JSON: " + e.getMessage());
                    return;
                }

                String jsonInput = jsonObject.toString();
                String jsonResponse = NetworkUtils.sendPostRequest(API_ENDPOINT, jsonInput);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleReporteResponse(jsonResponse);
                    }
                });
            }
        }).start();
    }

    private void handleReporteResponse(String response) {
        btnEnviarReporte.setEnabled(true);
        btnEnviarReporte.setText("Enviar Reporte");

        try {
            JSONObject jsonResponse = new JSONObject(response);
            boolean success = jsonResponse.getBoolean("success");
            String message = jsonResponse.getString("message");

            Toast.makeText(ReporteActivity.this, message, Toast.LENGTH_LONG).show();

            if (success) {
                // Reporte enviado: limpiar y cerrar
                etDescripcionReporte.setText("");
                // Puedes mostrar un diálogo de éxito si quieres antes de cerrar
                finish();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parseando JSON de respuesta del reporte: " + e.getMessage());
            Toast.makeText(ReporteActivity.this, "Error de comunicación con el servidor.", Toast.LENGTH_LONG).show();
        }
    }
}