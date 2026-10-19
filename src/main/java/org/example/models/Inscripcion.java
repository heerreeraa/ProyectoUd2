package org.example.models;

// Una jugadora apuntada a una partida: una socia (dniSocia) o una invitada (nombreInvitada)
public class Inscripcion {
    private int id;
    private int idPartida;
    private String dniSocia;
    private String nombreInvitada;
    private boolean ganadora;

    public Inscripcion() {
    }

    public Inscripcion(int idPartida, String dniSocia, String nombreInvitada, boolean ganadora) {
        this.idPartida = idPartida;
        this.dniSocia = dniSocia;
        this.nombreInvitada = nombreInvitada;
        this.ganadora = ganadora;
    }

    public Inscripcion(int id, int idPartida, String dniSocia, String nombreInvitada, boolean ganadora) {
        this.id = id;
        this.idPartida = idPartida;
        this.dniSocia = dniSocia;
        this.nombreInvitada = nombreInvitada;
        this.ganadora = ganadora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }

    public String getDniSocia() {
        return dniSocia;
    }

    public void setDniSocia(String dniSocia) {
        this.dniSocia = dniSocia;
    }

    public String getNombreInvitada() {
        return nombreInvitada;
    }

    public void setNombreInvitada(String nombreInvitada) {
        this.nombreInvitada = nombreInvitada;
    }

    public boolean isGanadora() {
        return ganadora;
    }

    public void setGanadora(boolean ganadora) {
        this.ganadora = ganadora;
    }

    @Override
    public String toString() {
        return (dniSocia != null ? "Socia " + dniSocia : "Invitada: " + nombreInvitada) + (ganadora ? " (GANADORA)" : "");
    }
}
