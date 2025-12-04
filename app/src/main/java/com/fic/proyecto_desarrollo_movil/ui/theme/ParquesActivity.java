package com.fic.proyecto_desarrollo_movil.ui.theme;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fic.proyecto_desarrollo_movil.Modelos.Parque;
import com.fic.proyecto_desarrollo_movil.R;
import com.fic.proyecto_desarrollo_movil.Adapters.ParqueAdapter;

import java.util.ArrayList;

public class ParquesActivity extends AppCompatActivity {

    RecyclerView recyclerParques;
    ParqueAdapter adapter;
    ArrayList<Parque> listaParques;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parques);

        recyclerParques = findViewById(R.id.recyclerParques);
        recyclerParques.setLayoutManager(new LinearLayoutManager(this));

        listaParques = new ArrayList<>();
        listaParques.add(new Parque("Parque Las Riberas", "Blvd. Francisco Labastida Ochoa S/N, Desarrollo Urbano Tres Ríos, C.P. 80230, Culiacán Rosales, Sinaloa"));
        listaParques.add(new Parque("Parque Revolución", "Paseo Niños Héroes entre Presa Vasequillos y Gral. Vicente Guerrero — Col. Centro / Oriente, cerca del río Tamazula."));
        listaParques.add(new Parque("Parque Constitución", "Zona Norte"));
        listaParques.add(new Parque("Parque 87","Av. México 68, Col. República Mexicana, C.P. 80147, Culiacán Rosales, Sinaloa"));
        listaParques.add(new Parque("Plazuela Rosales", "Centro Histórico"));
        listaParques.add(new Parque("Plazuela Alvaro Obregon", "Zona Centro"));


        adapter = new ParqueAdapter(listaParques, this);

        recyclerParques.setAdapter(adapter);
    }
}