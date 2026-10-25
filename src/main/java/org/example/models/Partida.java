package org.example.models;

import java.util.ArrayList;

public class Partida {
    private int id;
    private int idJuego;
    private String nombreJuego;
    private String fecha;
    private String hora;
    private int jugadoresDeseados;
    private boolean eliminada;
    private ArrayList<Inscripcion> jugadoras = new ArrayList<>();

    public Partida() {
    }

    public Partida(int idJuego, String nombreJuego, String fecha, String hora, int jugadoresDeseados, boolean eliminada) {
        this.idJuego = idJuego;
        this.nombreJuego = nombreJuego;
        this.fecha = fecha;
        this.hora = hora;
        this.jugadoresDeseados = jugadoresDeseados;
        this.eliminada = eliminada;
    }

    public Partida(int id, int idJuego, String nombreJuego, String fecha, String hora, int jugadoresDeseados, boolean eliminada) {
        this.id = id;
        this.idJuego = idJuego;
        this.nombreJuego = nombreJuego;
        this.fecha = fecha;
        this.hora = hora;
        this.jugadoresDeseados = jugadoresDeseados;
        this.eliminada = eliminada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdJuego() {
        return idJuego;
    }

    public void setIdJuego(int idJuego) {
        this.idJuego = idJuego;
    }

    public String getNombreJuego() {
        return nombreJuego;
    }

    public void setNombreJuego(String nombreJuego) {
        this.nombreJuego = nombreJuego;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getJugadoresDeseados() {
        return jugadoresDeseados;
    }

    public void setJugadoresDeseados(int jugadoresDeseados) {
        this.jugadoresDeseados = jugadoresDeseados;
    }

    public boolean isEliminada() {
        return eliminada;
    }

    public void setEliminada(boolean eliminada) {
        this.eliminada = eliminada;
    }

    public ArrayList<Inscripcion> getJugadoras() {
        return jugadoras;
    }

    public void setJugadoras(ArrayList<Inscripcion> jugadoras) {
        this.jugadoras = jugadoras;
    }

    public int getPlazasLibres() {
        return jugadoresDeseados - jugadoras.size();
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nombreJuego + " | " + fecha + " " + hora + " | Inscritas: " + jugadoras.size() + "/" + jugadoresDeseados
                + " | Plazas libres: " + getPlazasLibres() + (eliminada ? " | DADA DE BAJA" : "");
    }
}
