package org.example.dao;

import org.example.models.Socia;
import org.example.utils.ConexionBD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Los métodos lanzan SQLException para que el menú decida qué mensaje mostrar
public class SociaDaoImpl implements SociaDao {

    @Override
    public int insertar(Socia socia) throws SQLException {
        String query = "insert into socia (dni, nombre, apellidos, telefono, edad, email) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setString(1, socia.getDni());
        ps.setString(2, socia.getNombre());
        ps.setString(3, socia.getApellidos());
        ps.setString(4, socia.getTelefono());
        ps.setInt(5, socia.getEdad());
        ps.setString(6, socia.getEmail());
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }

    // Devuelve null si no existe
    @Override
    public Socia obtener(String dni) throws SQLException {
        String query = "select * from socia where dni = ?";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setString(1, dni);
        ResultSet rs = ps.executeQuery();
        Socia socia = null;
        if (rs.next()) {
            socia = crearSocia(rs);
        }
        rs.close();
        ps.close();
        return socia;
    }

    @Override
    public List<Socia> listar(boolean incluirEliminadas) throws SQLException {
        String query = "select * from socia";
        if (!incluirEliminadas) {
            query += " where eliminada = false";
        }
        query += " order by apellidos, nombre";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<Socia> socias = new ArrayList<>();
        while (rs.next()) {
            socias.add(crearSocia(rs));
        }
        rs.close();
        ps.close();
        return socias;
    }

    // Búsqueda por varios criterios: los que vienen vacíos (o -1) no se usan
    @Override
    public List<Socia> buscar(String nombre, String apellidos, int edadMinima, int edadMaxima) throws SQLException {
        String query = "select * from socia where eliminada = false";
        ArrayList<Object> parametros = new ArrayList<>();
        if (!nombre.isEmpty()) {
            query += " and nombre like ?";
            parametros.add("%" + nombre + "%");
        }
        if (!apellidos.isEmpty()) {
            query += " and apellidos like ?";
            parametros.add("%" + apellidos + "%");
        }
        if (edadMinima != -1) {
            query += " and edad >= ?";
            parametros.add(edadMinima);
        }
        if (edadMaxima != -1) {
            query += " and edad <= ?";
            parametros.add(edadMaxima);
        }
        query += " order by apellidos, nombre";

        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        // Se rellenan los ? en el mismo orden en que se han añadido
        for (int i = 0; i < parametros.size(); i++) {
            ps.setObject(i + 1, parametros.get(i));
        }
        ResultSet rs = ps.executeQuery();
        List<Socia> socias = new ArrayList<>();
        while (rs.next()) {
            socias.add(crearSocia(rs));
        }
        rs.close();
        ps.close();
        return socias;
    }

    // El DNI no se modifica porque es la clave primaria
    @Override
    public int modificar(Socia socia) throws SQLException {
        String query = "update socia set nombre = ?, apellidos = ?, telefono = ?, edad = ?, email = ? where dni = ?";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setString(1, socia.getNombre());
        ps.setString(2, socia.getApellidos());
        ps.setString(3, socia.getTelefono());
        ps.setInt(4, socia.getEdad());
        ps.setString(5, socia.getEmail());
        ps.setString(6, socia.getDni());
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }

    // Baja lógica: se marca como eliminada pero se conserva su historial
    @Override
    public int bajaLogica(String dni) throws SQLException {
        return cambiarEliminada(dni, true);
    }

    @Override
    public int reactivar(String dni) throws SQLException {
        return cambiarEliminada(dni, false);
    }

    // Baja física: si tiene juegos o inscripciones, MariaDB lo impide (error 1451)
    @Override
    public int bajaFisica(String dni) throws SQLException {
        String query = "delete from socia where dni = ?";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setString(1, dni);
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }

    private int cambiarEliminada(String dni, boolean eliminada) throws SQLException {
        String query = "update socia set eliminada = ? where dni = ?";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setBoolean(1, eliminada);
        ps.setString(2, dni);
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }

    // Pasa la fila actual del ResultSet a un objeto Socia
    private Socia crearSocia(ResultSet rs) throws SQLException {
        return new Socia(rs.getString("dni"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("telefono"),
                rs.getInt("edad"),
                rs.getString("email"),
                rs.getBoolean("eliminada"));
    }
}
