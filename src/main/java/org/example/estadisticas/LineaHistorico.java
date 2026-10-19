package org.example.estadisticas;

public class LineaHistorico {
    private String fecha;
    private String hora;
    private String juego;
    private boolean ganadora;

    // Constructor vacío para crear la línea antes de asignar sus datos.
    public LineaHistorico() {
    }

    // Devuelve la fecha de la partida.
    public String getFecha() {
        return fecha;
    }

    // Asigna la fecha de la partida.
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    // Devuelve la hora de la partida.
    public String getHora() {
        return hora;
    }

    // Asigna la hora de la partida.
    public void setHora(String hora) {
        this.hora = hora;
    }

    // Devuelve el nombre del juego.
    public String getJuego() {
        return juego;
    }

    // Asigna el nombre del juego.
    public void setJuego(String juego) {
        this.juego = juego;
    }

    // Indica si la socia ganó la partida.
    public boolean isGanadora() {
        return ganadora;
    }

    // Actualiza si la socia ganó la partida.
    public void setGanadora(boolean ganadora) {
        this.ganadora = ganadora;
    }

    // Devuelve los datos de la partida en una línea legible.
    @Override
    public String toString() {
        return fecha + " " + hora + " | " + juego + (ganadora ? " | Ganadora" : "");
    }
}
