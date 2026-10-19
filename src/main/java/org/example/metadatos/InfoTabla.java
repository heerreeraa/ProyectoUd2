package org.example.metadatos;

import java.util.ArrayList;

public class InfoTabla {
    private String esquema;
    private String nombre;
    private ArrayList<String> clavePrimaria = new ArrayList<>();
    private ArrayList<InfoColumna> columnas = new ArrayList<>();

    public InfoTabla() {
    }

    public String getEsquema() {
        return esquema;
    }

    public void setEsquema(String esquema) {
        this.esquema = esquema;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<String> getClavePrimaria() {
        return clavePrimaria;
    }

    public void setClavePrimaria(ArrayList<String> clavePrimaria) {
        this.clavePrimaria = clavePrimaria;
    }

    public ArrayList<InfoColumna> getColumnas() {
        return columnas;
    }

    public void setColumnas(ArrayList<InfoColumna> columnas) {
        this.columnas = columnas;
    }

    @Override
    public String toString() {
        return "Esquema: " + esquema + " | Tabla: " + nombre
                + "\nClave primaria: " + clavePrimaria
                + "\nColumnas: " + columnas;
    }
}
