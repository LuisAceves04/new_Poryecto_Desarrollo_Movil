package com.fic.proyecto_desarrollo_movil.ui.theme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.fic.proyecto_desarrollo_movil.R;


public class AdminPanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);


        Button btnAltaParque = findViewById(R.id.btnAltaParque);
        Button btnAltaAdmin = findViewById(R.id.btnAltaAdmin);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnAltaParque.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, AltaParqueActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
            prefs.edit().clear().apply();
            Intent intent = new Intent(AdminPanelActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}