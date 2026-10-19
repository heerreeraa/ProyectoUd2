package org.example.dao;

import org.example.models.Juego;
import org.example.utils.ConexionBD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class JuegoDaoImpl implements JuegoDao {

    @Override
    public int insertar(Juego juego) throws SQLException {
        String query = "insert into juego (nombre, autores, descripcion, min_jugadores, max_jugadores, duracion, tematica, "
                + "tipo, ubicacion, estado, dni_propietaria, fecha_alta) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        rellenarDatos(ps, juego);
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }

    @Override
    public Juego obtener(int id) throws SQLException {
        String query = "select * from juego where id = ?";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Juego juego = null;
        if (rs.next()) {
            juego = crearJuego(rs);
        }
        rs.close();
        ps.close();
        return juego;
    }

    @Override
    public List<Juego> listar(boolean incluirEliminados) throws SQLException {
        String query = "select * from juego";
        if (!incluirEliminados) {
            query += " where eliminado = false";
        }
        query += " order by id";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        return ejecutarLista(ps);
    }

    // Juegos que pertenecen a una socia
    @Override
    public List<Juego> listarPorPropietaria(String dni) throws SQLException {
        String query = "select * from juego where dni_propietaria = ? and eliminado = false order by nombre";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setString(1, dni);
        return ejecutarLista(ps);
    }

    // Búsqueda por varios criterios: los que vienen vacíos (o -1) no se usan
    @Override
    public List<Juego> buscar(String nombre, String tematica, String tipo, int jugadoras, int duracionMaxima,
                              String estado) throws SQLException {
        String query = "select * from juego where eliminado = false";
        ArrayList<Object> parametros = new ArrayList<>();
        if (!nombre.isEmpty()) {
            query += " and nombre like ?";
            parametros.add("%" + nombre + "%");
        }
        if (!tematica.isEmpty()) {
            query += " and tematica like ?";
            parametros.add("%" + tematica + "%");
        }
        if (!tipo.isEmpty()) {
            query += " and tipo = ?";
            parametros.add(tipo);
        }
        // Juegos en los que se puede jugar con ese número de jugadoras
        if (jugadoras != -1) {
            query += " and min_jugadores <= ? and max_jugadores >= ?";
            parametros.add(jugadoras);
            parametros.add(jugadoras);
        }
        if (duracionMaxima != -1) {
            query += " and duracion <= ?";
            parametros.add(duracionMaxima);
        }
        if (!estado.isEmpty()) {
            query += " and estado = ?";
            parametros.add(estado);
        }
        query += " order by nombre";

        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        // Se rellenan los ? en el mismo orden en que se han añadido
        for (int i = 0; i < parametros.size(); i++) {
            ps.setObject(i + 1, parametros.get(i));
        }
        return ejecutarLista(ps);
    }

    @Override
    public int modificar(Juego juego) throws SQLException {
        String query = "update juego set nombre = ?, autores = ?, descripcion = ?, min_jugadores = ?, max_jugadores = ?, "
                + "duracion = ?, tematica = ?, tipo = ?, ubicacion = ?, estado = ?, dni_propietaria = ?, fecha_alta = ? "
                + "where id = ?";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        rellenarDatos(ps, juego);
        ps.setInt(13, juego.getId());
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }

    @Override
    public int cambiarEstado(int id, String estado) throws SQLException {
        String query = "update juego set estado = ? where id = ?";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setString(1, estado);
        ps.setInt(2, id);
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }

    @Override
    public int bajaLogica(int id) throws SQLException {
        return cambiarEliminado(id, true);
    }

    @Override
    public int reactivar(int id) throws SQLException {
        return cambiarEliminado(id, false);
    }

    // Si el juego tiene partidas, MariaDB no deja borrarlo (error 1451)
    @Override
    public int bajaFisica(int id) throws SQLException {
        String query = "delete from juego where id = ?";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setInt(1, id);
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }

    private int cambiarEliminado(int id, boolean eliminado) throws SQLException {
        String query = "update juego set eliminado = ? where id = ?";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setBoolean(1, eliminado);
        ps.setInt(2, id);
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }

    // Los 12 primeros parámetros son iguales en el insert y en el update
    private void rellenarDatos(PreparedStatement ps, Juego juego) throws SQLException {
        ps.setString(1, juego.getNombre());
        ps.setString(2, juego.getAutores());
        ps.setString(3, juego.getDescripcion());
        ps.setInt(4, juego.getMinJugadores());
        ps.setInt(5, juego.getMaxJugadores());
        ps.setInt(6, juego.getDuracion());
        ps.setString(7, juego.getTematica());
        ps.setString(8, juego.getTipo());
        ps.setString(9, juego.getUbicacion());
        ps.setString(10, juego.getEstado());
        // Si no tiene propietaria se guarda NULL (es de la asociación)
        if (juego.getDniPropietaria() == null) {
            ps.setNull(11, Types.VARCHAR);
        } else {
            ps.setString(11, juego.getDniPropietaria());
        }
        ps.setString(12, juego.getFechaAlta());
    }

    private List<Juego> ejecutarLista(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        List<Juego> juegos = new ArrayList<>();
        while (rs.next()) {
            juegos.add(crearJuego(rs));
        }
        rs.close();
        ps.close();
        return juegos;
    }

    private Juego crearJuego(ResultSet rs) throws SQLException {
        return new Juego(rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("autores"),
                rs.getString("descripcion"),
                rs.getInt("min_jugadores"),
                rs.getInt("max_jugadores"),
                rs.getInt("duracion"),
                rs.getString("tematica"),
                rs.getString("tipo"),
                rs.getString("ubicacion"),
                rs.getString("estado"),
                rs.getString("dni_propietaria"),
                rs.getString("fecha_alta"),
                rs.getBoolean("eliminado"));
    }
}
