package com.fic.proyecto_desarrollo_movil.Modelos;

public class Parque {
    private String nombre;
    private String direccion;

    public Parque(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
}

