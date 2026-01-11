package com.fic.proyecto_desarrollo_movil.ui.theme;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.fic.proyecto_desarrollo_movil.R;

import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AltaParqueActivity extends AppCompatActivity {

    EditText etNombre, etUbicacion, etDescripcion;
    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_parque);

        etNombre = findViewById(R.id.etNombreParque);
        etUbicacion = findViewById(R.id.etUbicacionParque);
        etDescripcion = findViewById(R.id.etDescParque);
        btnGuardar = findViewById(R.id.btnGuardarParque);

        btnGuardar.setOnClickListener(v -> guardarParque());
    }

    private void guardarParque() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/parkmanager/api");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("nombre", etNombre.getText().toString());
                json.put("ubicacion", etUbicacion.getText().toString());
                json.put("descripcion", etDescripcion.getText().toString());

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = json.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int code = conn.getResponseCode();
                runOnUiThread(() -> {
                    if (code == 200 || code == 201) {
                        Toast.makeText(this, "Parque creado con éxito", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Error al crear parque", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}