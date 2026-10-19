package org.example.dao;

import org.example.models.Inscripcion;
import org.example.models.Partida;
import org.example.utils.ConexionBD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartidaDaoImpl implements PartidaDao {

    // Se une con juego para tener el nombre del juego sin hacer otra consulta
    private static final String SELECT_PARTIDA = "select p.*, j.nombre as nombre_juego from partida p "
            + "join juego j on p.id_juego = j.id";

    @Override
    public int insertar(Partida partida) throws SQLException {
        String query = "insert into partida (id_juego, fecha, hora, jugadores_deseados) values (?, ?, ?, ?)";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setInt(1, partida.getIdJuego());
        ps.setString(2, partida.getFecha());
        ps.setString(3, partida.getHora());
        ps.setInt(4, partida.getJugadoresDeseados());
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }

    // Devuelve la partida con sus jugadoras, o null si no existe
    @Override
    public Partida obtener(int id) throws SQLException {
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(SELECT_PARTIDA + " where p.id = ?");
        ps.setInt(1, id);
        List<Partida> partidas = ejecutarLista(ps);
        if (partidas.isEmpty()) {
            return null;
        }
        return partidas.get(0);
    }

    @Override
    public List<Partida> listar(boolean incluirEliminadas) throws SQLException {
        String query = SELECT_PARTIDA;
        if (!incluirEliminadas) {
            query += " where p.eliminada = false";
        }
        query += " order by p.fecha, p.hora";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        return ejecutarLista(ps);
    }

    // Partidas en las que está inscrita una socia
    @Override
    public List<Partida> listarPorSocia(String dni) throws SQLException {
        String query = SELECT_PARTIDA + " where p.eliminada = false and p.id in "
                + "(select id_partida from partida_jugadora where dni_socia = ?) order by p.fecha, p.hora";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setString(1, dni);
        return ejecutarLista(ps);
    }

    // Búsqueda por nombre del juego y por periodo; los criterios vacíos no se usan
    @Override
    public List<Partida> buscar(String juego, String desde, String hasta, boolean soloConPlazas) throws SQLException {
        String query = SELECT_PARTIDA + " where p.eliminada = false";
        ArrayList<Object> parametros = new ArrayList<>();
        if (!juego.isEmpty()) {
            query += " and j.nombre like ?";
            parametros.add("%" + juego + "%");
        }
        if (!desde.isEmpty()) {
            query += " and p.fecha >= ?";
            parametros.add(desde);
        }
        if (!hasta.isEmpty()) {
            query += " and p.fecha <= ?";
            parametros.add(hasta);
        }
        query += " order by p.fecha, p.hora";

        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        // Se rellenan los ? en el mismo orden en que se han añadido
        for (int i = 0; i < parametros.size(); i++) {
            ps.setObject(i + 1, parametros.get(i));
        }
        List<Partida> partidas = ejecutarLista(ps);

        // Las plazas libres no están en la BBDD, así que este filtro se hace en Java
        if (!soloConPlazas) {
            return partidas;
        }
        List<Partida> conPlazas = new ArrayList<>();
        for (Partida p : partidas) {
            if (p.getPlazasLibres() > 0) {
                conPlazas.add(p);
            }
        }
        return conPlazas;
    }

    @Override
    public int modificar(Partida partida) throws SQLException {
        String query = "update partida set id_juego = ?, fecha = ?, hora = ?, jugadores_deseados = ? where id = ?";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setInt(1, partida.getIdJuego());
        ps.setString(2, partida.getFecha());
        ps.setString(3, partida.getHora());
        ps.setInt(4, partida.getJugadoresDeseados());
        ps.setInt(5, partida.getId());
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }

    @Override
    public int bajaLogica(int id) throws SQLException {
        return cambiarEliminada(id, true);
    }

    @Override
    public int reactivar(int id) throws SQLException {
        return cambiarEliminada(id, false);
    }

    // Las inscripciones se borran solas por el ON DELETE CASCADE de partida_jugadora
    @Override
    public int bajaFisica(int id) throws SQLException {
        return ejecutarUpdate("delete from partida where id = ?", id);
    }

    @Override
    public int inscribirSocia(int idPartida, String dni) throws SQLException {
        String query = "insert into partida_jugadora (id_partida, dni_socia) values (?, ?)";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setInt(1, idPartida);
        ps.setString(2, dni);
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }

    @Override
    public int inscribirInvitada(int idPartida, String nombre) throws SQLException {
        String query = "insert into partida_jugadora (id_partida, nombre_invitada) values (?, ?)";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setInt(1, idPartida);
        ps.setString(2, nombre);
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }

    @Override
    public int desinscribir(int idInscripcion) throws SQLException {
        return ejecutarUpdate("delete from partida_jugadora where id = ?", idInscripcion);
    }

    @Override
    public int marcarGanadora(int idInscripcion, boolean ganadora) throws SQLException {
        String query = "update partida_jugadora set ganadora = ? where id = ?";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setBoolean(1, ganadora);
        ps.setInt(2, idInscripcion);
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }

    private int cambiarEliminada(int id, boolean eliminada) throws SQLException {
        String query = "update partida set eliminada = ? where id = ?";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setBoolean(1, eliminada);
        ps.setInt(2, id);
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }

    // Para los update/delete que solo reciben un id
    private int ejecutarUpdate(String query, int id) throws SQLException {
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setInt(1, id);
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }

    private List<Partida> ejecutarLista(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        List<Partida> partidas = new ArrayList<>();
        while (rs.next()) {
            Partida partida = new Partida(rs.getInt("id"),
                    rs.getInt("id_juego"),
                    rs.getString("nombre_juego"),
                    rs.getString("fecha"),
                    // La hora llega como "18:00:00"; nos quedamos con "18:00"
                    rs.getString("hora").substring(0, 5),
                    rs.getInt("jugadores_deseados"),
                    rs.getBoolean("eliminada"));
            partidas.add(partida);
        }
        rs.close();
        ps.close();

        // Después de cerrar el ResultSet, se cargan las jugadoras de cada partida
        for (Partida p : partidas) {
            p.setJugadoras(obtenerJugadoras(p.getId()));
        }
        return partidas;
    }

    private ArrayList<Inscripcion> obtenerJugadoras(int idPartida) throws SQLException {
        String query = "select * from partida_jugadora where id_partida = ? order by id";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setInt(1, idPartida);
        ResultSet rs = ps.executeQuery();
        ArrayList<Inscripcion> jugadoras = new ArrayList<>();
        while (rs.next()) {
            jugadoras.add(new Inscripcion(rs.getInt("id"),
                    rs.getInt("id_partida"),
                    rs.getString("dni_socia"),
                    rs.getString("nombre_invitada"),
                    rs.getBoolean("ganadora")));
        }
        rs.close();
        ps.close();
        return jugadoras;
    }
}
