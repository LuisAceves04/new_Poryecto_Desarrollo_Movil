package com.fic.proyecto_desarrollo_movil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.fic.proyecto_desarrollo_movil.HomeActivity;
import com.fic.proyecto_desarrollo_movil.ui.theme.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verificar si ya hay una sesión activa
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            // Si ya está logueado, ir directamente a HomeActivity
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else {
            // Si no está logueado, ir a LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        finish();
    }
}