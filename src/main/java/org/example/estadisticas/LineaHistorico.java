package org.example.estadisticas;

public class LineaHistorico {
    private String fecha;
    private String hora;
    private String juego;
    private boolean ganadora;

    public LineaHistorico() {
    }

    public LineaHistorico(String fecha, String hora, String juego, boolean ganadora) {
        this.fecha = fecha;
        this.hora = hora;
        this.juego = juego;
        this.ganadora = ganadora;
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

    public String getJuego() {
        return juego;
    }

    public void setJuego(String juego) {
        this.juego = juego;
    }

    public boolean isGanadora() {
        return ganadora;
    }

    public void setGanadora(boolean ganadora) {
        this.ganadora = ganadora;
    }

    @Override
    public String toString() {
        return fecha + " " + hora + " | " + juego + (ganadora ? " | GANADORA" : "");
    }
}
