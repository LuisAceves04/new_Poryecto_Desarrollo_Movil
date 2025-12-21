package com.fic.proyecto_desarrollo_movil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fic.proyecto_desarrollo_movil.ui.theme.MisReportesActivity;
import com.fic.proyecto_desarrollo_movil.ui.theme.LoginActivity;
import com.fic.proyecto_desarrollo_movil.ui.theme.ParquesActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // verificar si el usuario esta logueado
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

        if (!isLoggedIn) {
            // Si no está logueado, redirigir al Login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Inicializar MapView
        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        Button btnLogout = findViewById(R.id.btn_CerrarSesion);

        if (btnLogout != null) {
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });
        }

        setupButtons();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        // Centrar en Culiacán
        LatLng culiacan = new LatLng(24.806, -107.394);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(culiacan, 12));

        addParkMarkers();
        Toast.makeText(this, "Mapa de Culiacán cargado", Toast.LENGTH_SHORT).show();
    }

    //MÉTODOS DEL CICLO DE VIDA PARA MAPVIEW
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

        // VERIFICAR NUEVAMENTE SI SIGUE LOGUEADO
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

        if (!isLoggedIn) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void addParkMarkers() {
        LatLng parqueLasRiberas = new LatLng(24.806, -107.394);
        mMap.addMarker(new MarkerOptions()
                .position(parqueLasRiberas)
                .title("Parque Las Riberas")
                .snippet("Parque lineal junto al río"));

        LatLng parqueRevolucion = new LatLng(24.809, -107.392);
        mMap.addMarker(new MarkerOptions()
                .position(parqueRevolucion)
                .title("Parque Revolución")
                .snippet("Parque histórico en el centro"));

        LatLng parqueConstitución = new LatLng(24.803, -107.389);
        mMap.addMarker(new MarkerOptions()
                .position(parqueConstitución)
                .title("Parque Constitución")
                .snippet("Área verde recreativa"));
    }

    private void setupButtons() {
        Button btnParques = findViewById(R.id.btn_Parques);
        Button btnMisReportes = findViewById(R.id.btn_Reportes);

        if (btnParques != null) {
            btnParques.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, ParquesActivity.class);
                startActivity(intent);
            });
        }

        if (btnMisReportes != null) {
            btnMisReportes.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, MisReportesActivity.class);
                startActivity(intent);
            });
        }
    }


    // metodo de cerrar sesion
    private void logout() {
        // Limpiar la sesión
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}