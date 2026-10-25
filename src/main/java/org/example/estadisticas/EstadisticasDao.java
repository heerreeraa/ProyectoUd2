package org.example.estadisticas;

import org.example.utils.ConexionBD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EstadisticasDao {

    private static final String INSCRIPCIONES_PERIODO = " from partida_jugadora pj"
            + " join partida p on pj.id_partida = p.id"
            + " where p.eliminada = false and p.fecha between ? and ?";
    private static final String INSCRIPCIONES_CON_JUEGO = " from partida_jugadora pj"
            + " join partida p on pj.id_partida = p.id"
            + " join juego j on p.id_juego = j.id"
            + " where p.eliminada = false and p.fecha between ? and ?";
    private static final String INSCRIPCIONES_CON_SOCIA = " from partida_jugadora pj"
            + " join partida p on pj.id_partida = p.id"
            + " join socia s on pj.dni_socia = s.dni"
            + " where p.eliminada = false and p.fecha between ? and ?";

    public HistoricoSocia historicoSocia(String dni, String desde, String hasta) throws SQLException {
        PreparedStatement ps = ConexionBD.getInstance().getConnection()
                .prepareStatement("select nombre, apellidos from socia where dni = ?");
        ps.setString(1, dni);
        ResultSet rs = ps.executeQuery();
        HistoricoSocia historico = null;
        if (rs.next()) {
            historico = new HistoricoSocia();
            historico.setDni(dni);
            historico.setNombre(rs.getString("nombre") + " " + rs.getString("apellidos"));
            historico.setDesde(desde);
            historico.setHasta(hasta);
        }
        rs.close();
        ps.close();
        if (historico == null) {
            return null;
        }

        String query = "select p.fecha, p.hora, j.nombre, pj.ganadora" + INSCRIPCIONES_CON_JUEGO
                + " and pj.dni_socia = ? order by p.fecha, p.hora";
        ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setString(1, desde);
        ps.setString(2, hasta);
        ps.setString(3, dni);
        rs = ps.executeQuery();
        int victorias = 0;
        while (rs.next()) {
            LineaHistorico linea = new LineaHistorico(rs.getString("fecha"),
                    rs.getString("hora").substring(0, 5),
                    rs.getString("nombre"),
                    rs.getBoolean("ganadora"));
            if (linea.isGanadora()) {
                victorias++;
            }
            historico.getPartidas().add(linea);
        }
        rs.close();
        ps.close();

        int jugadas = historico.getPartidas().size();
        historico.setPartidasJugadas(jugadas);
        historico.setVictorias(victorias);
        if (jugadas > 0) {
            historico.setPorcentajeVictorias(victorias * 100.0 / jugadas);
        }

        query = "select j.nombre, count(*) as total" + INSCRIPCIONES_CON_JUEGO
                + " and pj.dni_socia = ? group by j.id, j.nombre order by total desc limit 1";
        historico.setJuegoMasJugado(primerResultado(query, desde, hasta, dni, "partida"));
        return historico;
    }

    public EstadisticasAsociacion estadisticasAsociacion(String desde, String hasta) throws SQLException {
        EstadisticasAsociacion e = new EstadisticasAsociacion();
        e.setDesde(desde);
        e.setHasta(hasta);

        e.setSociasActivas(contar("select count(*) from socia where eliminada = false"));
        e.setCompetitivos(contar("select count(*) from juego where eliminado = false and tipo = 'COMPETITIVO'"));
        e.setCooperativos(contar("select count(*) from juego where eliminado = false and tipo = 'COOPERATIVO'"));
        e.setJuegosAsociacion(contar("select count(*) from juego where eliminado = false and dni_propietaria is null"));
        e.setJuegosDeSocias(contar("select count(*) from juego where eliminado = false and dni_propietaria is not null"));
        e.setJuegosPorEstado(juegosPorEstado());

        int partidas = contarPeriodo("select count(*) from partida p where p.eliminada = false and p.fecha between ? and ?", desde, hasta);
        int inscritas = contarPeriodo("select count(*)" + INSCRIPCIONES_PERIODO, desde, hasta);
        int deseadas = contarPeriodo("select sum(p.jugadores_deseados) from partida p where p.eliminada = false and p.fecha between ? and ?", desde, hasta);
        e.setPartidasJugadas(partidas);
        e.setInvitadas(contarPeriodo("select count(*)" + INSCRIPCIONES_PERIODO + " and pj.dni_socia is null", desde, hasta));
        if (partidas > 0) {
            e.setMediaJugadorasPorPartida((double) inscritas / partidas);
        }
        if (deseadas > 0) {
            e.setOcupacionMedia(inscritas * 100.0 / deseadas);
        }

        String query = "select j.nombre, count(*) as total from partida p join juego j on p.id_juego = j.id"
                + " where p.eliminada = false and p.fecha between ? and ? group by j.id, j.nombre order by total desc limit 5";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setString(1, desde);
        ps.setString(2, hasta);
        ResultSet rs = ps.executeQuery();
        ArrayList<String> juegos = new ArrayList<>();
        while (rs.next()) {
            juegos.add(rs.getString("nombre") + " (" + cantidad(rs.getInt("total"), "partida") + ")");
        }
        rs.close();
        ps.close();
        e.setJuegosMasJugados(juegos);

        String consultaSocia = "select concat(s.nombre, ' ', s.apellidos) as nombre, count(*) as total" + INSCRIPCIONES_CON_SOCIA;
        e.setSociaConMasPartidas(primerResultado(consultaSocia + " group by s.dni order by total desc limit 1",
                desde, hasta, null, "partida"));
        e.setSociaConMasVictorias(primerResultado(consultaSocia + " and pj.ganadora = true group by s.dni order by total desc limit 1",
                desde, hasta, null, "victoria"));
        return e;
    }

    private String juegosPorEstado() throws SQLException {
        String query = "select estado, count(*) as total from juego where eliminado = false group by estado order by estado";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        String texto = "";
        while (rs.next()) {
            if (!texto.isEmpty()) {
                texto += ", ";
            }
            texto += rs.getString("estado") + ": " + rs.getInt("total");
        }
        rs.close();
        ps.close();
        return texto;
    }

    private int contar(String query) throws SQLException {
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int total = rs.getInt(1);
        rs.close();
        ps.close();
        return total;
    }

    private int contarPeriodo(String query, String desde, String hasta) throws SQLException {
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setString(1, desde);
        ps.setString(2, hasta);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int total = rs.getInt(1);
        rs.close();
        ps.close();
        return total;
    }

    private String cantidad(int numero, String palabra) {
        if (numero == 1) {
            return numero + " " + palabra;
        }
        return numero + " " + palabra + "s";
    }

    private String primerResultado(String query, String desde, String hasta, String dni, String unidad) throws SQLException {
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setString(1, desde);
        ps.setString(2, hasta);
        if (dni != null) {
            ps.setString(3, dni);
        }
        ResultSet rs = ps.executeQuery();
        String resultado = "Ninguna";
        if (rs.next()) {
            resultado = rs.getString("nombre") + " (" + cantidad(rs.getInt("total"), unidad) + ")";
        }
        rs.close();
        ps.close();
        return resultado;
    }
}
