package org.example.estadisticas;

import java.util.ArrayList;

public class HistoricoSocia {
    private String dni;
    private String nombre;
    private String desde;
    private String hasta;
    private int partidasJugadas;
    private int victorias;
    private double porcentajeVictorias;
    private String juegoMasJugado;
    private ArrayList<LineaHistorico> partidas = new ArrayList<>();

    public HistoricoSocia() {
    }

    public HistoricoSocia(String dni, String nombre, String desde, String hasta, int partidasJugadas, int victorias, double porcentajeVictorias, String juegoMasJugado) {
        this.dni = dni;
        this.nombre = nombre;
        this.desde = desde;
        this.hasta = hasta;
        this.partidasJugadas = partidasJugadas;
        this.victorias = victorias;
        this.porcentajeVictorias = porcentajeVictorias;
        this.juegoMasJugado = juegoMasJugado;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDesde() {
        return desde;
    }

    public void setDesde(String desde) {
        this.desde = desde;
    }

    public String getHasta() {
        return hasta;
    }

    public void setHasta(String hasta) {
        this.hasta = hasta;
    }

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public void setPartidasJugadas(int partidasJugadas) {
        this.partidasJugadas = partidasJugadas;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    public double getPorcentajeVictorias() {
        return porcentajeVictorias;
    }

    public void setPorcentajeVictorias(double porcentajeVictorias) {
        this.porcentajeVictorias = porcentajeVictorias;
    }

    public String getJuegoMasJugado() {
        return juegoMasJugado;
    }

    public void setJuegoMasJugado(String juegoMasJugado) {
        this.juegoMasJugado = juegoMasJugado;
    }

    public ArrayList<LineaHistorico> getPartidas() {
        return partidas;
    }

    public void setPartidas(ArrayList<LineaHistorico> partidas) {
        this.partidas = partidas;
    }

    @Override
    public String toString() {
        return "Histórico de " + nombre + " [" + dni + "] del " + desde + " al " + hasta + "\n"
                + "Partidas: " + partidasJugadas + " | Victorias: " + victorias + " (" + Math.round(porcentajeVictorias) + "%)\n"
                + "Juego más jugado: " + juegoMasJugado;
    }
}
