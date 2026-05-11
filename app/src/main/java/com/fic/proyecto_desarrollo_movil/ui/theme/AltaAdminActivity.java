package com.fic.proyecto_desarrollo_movil.ui.theme;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fic.proyecto_desarrollo_movil.R;

import java.util.HashMap;
import java.util.Map;

public class AltaAdminActivity extends AppCompatActivity {
    // 1. Declarar variables
    private EditText etNombreAdmin, etApellidoAdmin, etCorreoAdmin, etPass;
    private Button btnGuardarAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_admin);

        // 2. Enlazar con el XML (¡ESTO FALTABA!)
        etNombreAdmin = findViewById(R.id.etNombreAdmin);
        etApellidoAdmin = findViewById(R.id.etApellidoAdmin);
        etCorreoAdmin = findViewById(R.id.etCorreoAdmin);
        etPass = findViewById(R.id.etPasswordAdmin);
        btnGuardarAdmin = findViewById(R.id.btnGuardarAdmin);

        // 3. Configurar el evento del botón
        btnGuardarAdmin.setOnClickListener(v -> {
            if (validarCampos()) {
                registrarServidor();
            }
        });
    }

    private boolean validarCampos() {
        if (etNombreAdmin.getText().toString().isEmpty() ||
                etApellidoAdmin.getText().toString().isEmpty() ||
                etCorreoAdmin.getText().toString().isEmpty() ||
                etPass.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registrarServidor() {
        String URL = "http://192.168.100.30/parkmanager/api/registrar_admin.php";

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    // Imprimimos la respuesta completa en el Logcat para ver si hay errores de PHP
                    android.util.Log.d("SERVIDOR_RESPUESTA", response);

                    if (response.trim().equals("registro_exitoso")) {
                        Toast.makeText(this, "Administrador creado correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Revisa el Logcat para ver el error", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    // Si hay un error de red o el PHP explota (Error 500)
                    if (error.networkResponse != null) {
                        String errorHtml = new String(error.networkResponse.data);
                        android.util.Log.e("DATABASE_ERROR", "Error del PHP: " + errorHtml);
                    }
                    Toast.makeText(this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("nombre", etNombreAdmin.getText().toString().trim());
                parametros.put("apellido", etApellidoAdmin.getText().toString().trim());
                parametros.put("correo", etCorreoAdmin.getText().toString().trim());

                parametros.put("contrasena", etPass.getText().toString().trim());

                parametros.put("rol", "admin");
                return parametros;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}