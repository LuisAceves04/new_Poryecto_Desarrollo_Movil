package com.fic.proyecto_desarrollo_movil.ui.theme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fic.proyecto_desarrollo_movil.HomeActivity;
import com.fic.proyecto_desarrollo_movil.R;
import com.fic.proyecto_desarrollo_movil.ui.theme.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText etCorreo, etPassword;
    private Button btnLogin;
    private TextView tvRegistrar;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);

        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegistrar = findViewById(R.id.tvRegistrar);

        // BOTÓN DEL REGISTRO
        tvRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        checkExistingSession();
    }

    private void checkExistingSession() {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        if (prefs.getBoolean("is_logged_in", false)) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void attemptLogin() {
        String email = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etCorreo.setError("Ingresa tu correo electrónico");
            etCorreo.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Ingresa tu contraseña");
            etPassword.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etCorreo.setError("Ingresa un correo válido");
            etCorreo.requestFocus();
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Iniciando sesión...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                String response = performLogin(email, password);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleLoginResponse(response, email);
                    }
                });
            }
        }).start();
    }

    private String performLogin(String email, String password) {
        HttpURLConnection connection = null;
        try {
            // Crear JSON para el login
            JSONObject jsonLogin = new JSONObject();
            jsonLogin.put("correo", email);
            jsonLogin.put("contrasena", password);

            String jsonInput = jsonLogin.toString();
            Log.d(TAG, "JSON a enviar: " + jsonInput);

            // URL completa
            String urlCompleta = "http://192.168.100.27/parkmanager/api/login.php";
            Log.d(TAG, "URL completa: " + urlCompleta);

            URL url = new URL(urlCompleta);
            connection = (HttpURLConnection) url.openConnection();

            // Configurar la conexión
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            Log.d(TAG, "Conectando...");

            // Escribir JSON
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
                Log.d(TAG, "JSON enviado");
            }

            // Obtener respuesta
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "Código HTTP: " + responseCode);

            // Leer respuesta
            BufferedReader in;
            if (responseCode >= 200 && responseCode < 300) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String respuestaFinal = response.toString();
            Log.d(TAG, "Respuesta CRUDA: " + respuestaFinal);

            return respuestaFinal;

        } catch (Exception e) {
            Log.e(TAG, "Error COMPLETO: " + e.toString());
            e.printStackTrace();
            return "{\"success\":false,\"message\":\"Error: " + e.getMessage() + "\"}";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void handleLoginResponse(String response, String email) {
        Log.d(TAG, " INICIANDO handleLoginResponse");
        Log.d(TAG, " Respuesta del servidor: " + response);

        btnLogin.setEnabled(true);
        btnLogin.setText("Iniciar Sesión");

        try {
            JSONObject jsonResponse = new JSONObject(response);
            boolean success = jsonResponse.getBoolean("success");

            Log.d(TAG, "Success: " + success);

            if (success) {
                String message = jsonResponse.getString("message");
                Log.d(TAG, "Mensaje: " + message);

                saveUserSession(jsonResponse, email);

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();

            } else {
                String errorMessage = "Credenciales incorrectas";
                if (jsonResponse.has("message")) {
                    errorMessage = jsonResponse.getString("message");
                }
                Log.d(TAG, "Error del servidor: " + errorMessage);
                Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parseando JSON: " + e.getMessage());
            Log.e(TAG, "Respuesta que falló: " + response);
            Toast.makeText(this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserSession(JSONObject jsonResponse, String email) {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("is_logged_in", true);
        editor.putString("user_email", email);

        try {
            if (jsonResponse.has("usuario")) {
                JSONObject userData = jsonResponse.getJSONObject("usuario");

                if (userData.has("id")) {
                    editor.putInt("USER_ID", userData.getInt("id"));
                    Log.d(TAG, "ID de usuario guardado: " + userData.getInt("id"));
                }

                if (userData.has("nombre")) {
                    editor.putString("user_name", userData.getString("nombre"));
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error guardando datos adicionales del usuario: " + e.getMessage());
        }

        editor.apply();
        Log.d(TAG, " Sesión guardada para: " + email);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnLogin.setEnabled(true);
        btnLogin.setText("Iniciar Sesión");
    }
}