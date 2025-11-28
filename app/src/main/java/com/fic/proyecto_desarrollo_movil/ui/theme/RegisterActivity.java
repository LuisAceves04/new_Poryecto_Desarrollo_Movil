package com.fic.proyecto_desarrollo_movil.ui.theme;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.fic.proyecto_desarrollo_movil.ui.theme.NetworkUtils;
import com.fic.proyecto_desarrollo_movil.R;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNombreR, etApellidoR, etEmailR, etPasswordR;
    private Button btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_usuario);

        etNombreR = findViewById(R.id.etNombreR);
        etApellidoR = findViewById(R.id.etApellidoR);
        etEmailR = findViewById(R.id.etEmailR);
        etPasswordR = findViewById(R.id.etPasswordR);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        //Obtener lo que escribió el usuario
        String nombre = etNombreR.getText().toString().trim();
        String apellido = etApellidoR.getText().toString().trim();
        String correo = etEmailR.getText().toString().trim();
        String contrasena = etPasswordR.getText().toString().trim();

        //Verificar que no estén vacíos
        if (nombre.isEmpty()) {
            etNombreR.setError("El nombre es requerido");
            etNombreR.requestFocus();
            return;
        }

        if (apellido.isEmpty()) {
            etApellidoR.setError("El apellido es requerido");
            etApellidoR.requestFocus();
            return;
        }

        if (correo.isEmpty()) {
            etEmailR.setError("El correo es requerido");
            etEmailR.requestFocus();
            return;
        }

        if (contrasena.isEmpty()) {
            etPasswordR.setError("La contraseña es requerida");
            etPasswordR.requestFocus();
            return;
        }

        if (contrasena.length() < 6) {
            etPasswordR.setError("La contraseña debe tener al menos 6 caracteres");
            etPasswordR.requestFocus();
            return;
        }

        btnRegistrarse.setEnabled(false);
        btnRegistrarse.setText("Registrando...");

        //ENVIAR DATOS AL SERVIDOR
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Preparar los datos para enviar
                    JSONObject datosUsuario = new JSONObject();
                    datosUsuario.put("nombre", nombre);
                    datosUsuario.put("apellido", apellido);
                    datosUsuario.put("correo", correo);
                    datosUsuario.put("contrasena", contrasena);

                    // Enviar al servidor y obtener respuesta
                    String respuestaServidor = NetworkUtils.sendPostRequest("register.php", datosUsuario.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mostrarResultado(respuestaServidor);
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Error de conexión", Toast.LENGTH_LONG).show();
                            btnRegistrarse.setEnabled(true);
                            btnRegistrarse.setText("Registrarse");
                        }
                    });
                }
            }
        }).start();
    }

    private void mostrarResultado(String respuesta) {
        try {

            JSONObject respuestaJson = new JSONObject(respuesta);
            boolean exito = respuestaJson.getBoolean("success");
            String mensaje = respuestaJson.getString("message");

            if (exito) {
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error del servidor", Toast.LENGTH_LONG).show();
        } finally {
            btnRegistrarse.setEnabled(true);
            btnRegistrarse.setText("Registrarse");
        }
    }
}