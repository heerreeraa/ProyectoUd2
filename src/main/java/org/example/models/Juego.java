package org.example.models;

public class Juego {
    private int id;
    private String nombre;
    private String autores;
    private String descripcion;
    private int minJugadores;
    private int maxJugadores;
    private int duracion;
    private String tematica;
    private String tipo;
    private String ubicacion;
    private String estado;
    private String dniPropietaria;
    private String fechaAlta;
    private boolean eliminado;

    public Juego() {
    }

    public Juego(String nombre, String autores, String descripcion, int minJugadores, int maxJugadores, int duracion, String tematica, String tipo, String ubicacion, String estado, String dniPropietaria, String fechaAlta, boolean eliminado) {
        this.nombre = nombre;
        this.autores = autores;
        this.descripcion = descripcion;
        this.minJugadores = minJugadores;
        this.maxJugadores = maxJugadores;
        this.duracion = duracion;
        this.tematica = tematica;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.estado = estado;
        this.dniPropietaria = dniPropietaria;
        this.fechaAlta = fechaAlta;
        this.eliminado = eliminado;
    }

    public Juego(int id, String nombre, String autores, String descripcion, int minJugadores, int maxJugadores, int duracion, String tematica, String tipo, String ubicacion, String estado, String dniPropietaria, String fechaAlta, boolean eliminado) {
        this.id = id;
        this.nombre = nombre;
        this.autores = autores;
        this.descripcion = descripcion;
        this.minJugadores = minJugadores;
        this.maxJugadores = maxJugadores;
        this.duracion = duracion;
        this.tematica = tematica;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.estado = estado;
        this.dniPropietaria = dniPropietaria;
        this.fechaAlta = fechaAlta;
        this.eliminado = eliminado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutores() {
        return autores;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getMinJugadores() {
        return minJugadores;
    }

    public void setMinJugadores(int minJugadores) {
        this.minJugadores = minJugadores;
    }

    public int getMaxJugadores() {
        return maxJugadores;
    }

    public void setMaxJugadores(int maxJugadores) {
        this.maxJugadores = maxJugadores;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getTematica() {
        return tematica;
    }

    public void setTematica(String tematica) {
        this.tematica = tematica;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDniPropietaria() {
        return dniPropietaria;
    }

    public void setDniPropietaria(String dniPropietaria) {
        this.dniPropietaria = dniPropietaria;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nombre + " (" + autores + ")\n"
                + "    " + minJugadores + "-" + maxJugadores + " jugadoras | " + duracion + " min | " + tematica + " | " + tipo + "\n"
                + "    Ubicación: " + ubicacion + " | Estado: " + estado
                + " | Propietaria: " + (dniPropietaria == null ? "Asociación" : dniPropietaria)
                + " | Alta: " + fechaAlta + (eliminado ? " | DADO DE BAJA" : "");
    }
}
