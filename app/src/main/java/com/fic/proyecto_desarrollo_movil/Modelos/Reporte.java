package com.fic.proyecto_desarrollo_movil.Modelos;
public class Reporte {
    private String nombreParque;
    private String descripcion;
    private String fecha;

    public Reporte(String nombreParque, String descripcion, String fecha) {
        this.nombreParque = nombreParque;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    // Getters
    public String getNombreParque() { return nombreParque; }
    public String getDescripcion() { return descripcion; }
    public String getFecha() { return fecha; }
}