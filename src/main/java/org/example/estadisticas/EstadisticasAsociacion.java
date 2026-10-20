package org.example.estadisticas;

import java.util.ArrayList;

// Estadísticas generales de la asociación en un periodo
public class EstadisticasAsociacion {
    private String desde;
    private String hasta;
    private int sociasActivas;
    private int partidasJugadas;
    private double mediaJugadorasPorPartida;
    private double ocupacionMedia;
    private int invitadas;
    private ArrayList<String> juegosMasJugados = new ArrayList<>();
    private String sociaConMasPartidas;
    private String sociaConMasVictorias;
    private String juegosPorEstado;
    private int competitivos;
    private int cooperativos;
    private int juegosAsociacion;
    private int juegosDeSocias;

    public EstadisticasAsociacion() {
    }

    public EstadisticasAsociacion(String desde, String hasta, int sociasActivas, int partidasJugadas, double mediaJugadorasPorPartida, double ocupacionMedia, int invitadas, String sociaConMasPartidas, String sociaConMasVictorias, String juegosPorEstado, int competitivos, int cooperativos, int juegosAsociacion, int juegosDeSocias) {
        this.desde = desde;
        this.hasta = hasta;
        this.sociasActivas = sociasActivas;
        this.partidasJugadas = partidasJugadas;
        this.mediaJugadorasPorPartida = mediaJugadorasPorPartida;
        this.ocupacionMedia = ocupacionMedia;
        this.invitadas = invitadas;
        this.sociaConMasPartidas = sociaConMasPartidas;
        this.sociaConMasVictorias = sociaConMasVictorias;
        this.juegosPorEstado = juegosPorEstado;
        this.competitivos = competitivos;
        this.cooperativos = cooperativos;
        this.juegosAsociacion = juegosAsociacion;
        this.juegosDeSocias = juegosDeSocias;
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

    public int getSociasActivas() {
        return sociasActivas;
    }

    public void setSociasActivas(int sociasActivas) {
        this.sociasActivas = sociasActivas;
    }

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public void setPartidasJugadas(int partidasJugadas) {
        this.partidasJugadas = partidasJugadas;
    }

    public double getMediaJugadorasPorPartida() {
        return mediaJugadorasPorPartida;
    }

    public void setMediaJugadorasPorPartida(double mediaJugadorasPorPartida) {
        this.mediaJugadorasPorPartida = mediaJugadorasPorPartida;
    }

    public double getOcupacionMedia() {
        return ocupacionMedia;
    }

    public void setOcupacionMedia(double ocupacionMedia) {
        this.ocupacionMedia = ocupacionMedia;
    }

    public int getInvitadas() {
        return invitadas;
    }

    public void setInvitadas(int invitadas) {
        this.invitadas = invitadas;
    }

    public ArrayList<String> getJuegosMasJugados() {
        return juegosMasJugados;
    }

    public void setJuegosMasJugados(ArrayList<String> juegosMasJugados) {
        this.juegosMasJugados = juegosMasJugados;
    }

    public String getSociaConMasPartidas() {
        return sociaConMasPartidas;
    }

    public void setSociaConMasPartidas(String sociaConMasPartidas) {
        this.sociaConMasPartidas = sociaConMasPartidas;
    }

    public String getSociaConMasVictorias() {
        return sociaConMasVictorias;
    }

    public void setSociaConMasVictorias(String sociaConMasVictorias) {
        this.sociaConMasVictorias = sociaConMasVictorias;
    }

    public String getJuegosPorEstado() {
        return juegosPorEstado;
    }

    public void setJuegosPorEstado(String juegosPorEstado) {
        this.juegosPorEstado = juegosPorEstado;
    }

    public int getCompetitivos() {
        return competitivos;
    }

    public void setCompetitivos(int competitivos) {
        this.competitivos = competitivos;
    }

    public int getCooperativos() {
        return cooperativos;
    }

    public void setCooperativos(int cooperativos) {
        this.cooperativos = cooperativos;
    }

    public int getJuegosAsociacion() {
        return juegosAsociacion;
    }

    public void setJuegosAsociacion(int juegosAsociacion) {
        this.juegosAsociacion = juegosAsociacion;
    }

    public int getJuegosDeSocias() {
        return juegosDeSocias;
    }

    public void setJuegosDeSocias(int juegosDeSocias) {
        this.juegosDeSocias = juegosDeSocias;
    }

    @Override
    public String toString() {
        // La lista de juegos se pone separada por comas, o "Ninguno" si no hay partidas
        String juegos = "Ninguno";
        if (!juegosMasJugados.isEmpty()) {
            juegos = String.join(", ", juegosMasJugados);
        }
        return "Estadísticas del " + desde + " al " + hasta + "\n"
                + "--- Partidas del periodo ---\n"
                + "Partidas: " + partidasJugadas + " | Media de jugadoras por partida: " + Math.round(mediaJugadorasPorPartida * 10) / 10.0
                + " | Ocupación media: " + Math.round(ocupacionMedia) + "% | Invitadas: " + invitadas + "\n"
                + "Juegos más jugados: " + juegos + "\n"
                + "Socia con más partidas: " + sociaConMasPartidas + "\n"
                + "Socia con más victorias: " + sociaConMasVictorias + "\n"
                + "--- Situación actual ---\n"
                + "Socias activas: " + sociasActivas + "\n"
                + "Juegos por estado: " + juegosPorEstado + "\n"
                + "Competitivos: " + competitivos + " | Cooperativos: " + cooperativos + "\n"
                + "De la asociación: " + juegosAsociacion + " | De socias: " + juegosDeSocias;
    }
}
