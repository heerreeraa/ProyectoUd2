package org.example.dao;

import org.example.models.Asociacion;
import org.example.utils.ConexionBD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AsociacionDaoImpl implements AsociacionDao {

    @Override
    public Asociacion obtener() throws SQLException {
        String query = "select * from asociacion where id = 1";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        Asociacion asociacion = null;
        if (rs.next()) {
            asociacion = new Asociacion(rs.getInt("id"),
                    rs.getString("razon_social"),
                    rs.getString("cif"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("fecha_apertura"));
        }
        rs.close();
        ps.close();
        return asociacion;
    }

    @Override
    public int modificar(Asociacion asociacion) throws SQLException {
        String query = "update asociacion set razon_social = ?, cif = ?, direccion = ?, telefono = ?, email = ?, "
                + "fecha_apertura = ? where id = 1";
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement(query);
        ps.setString(1, asociacion.getRazonSocial());
        ps.setString(2, asociacion.getCif());
        ps.setString(3, asociacion.getDireccion());
        ps.setString(4, asociacion.getTelefono());
        ps.setString(5, asociacion.getEmail());
        ps.setString(6, asociacion.getFechaApertura());
        int filas = ps.executeUpdate();
        ps.close();
        return filas;
    }
}
