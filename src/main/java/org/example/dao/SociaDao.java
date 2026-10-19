package org.example.dao;

import org.example.models.Socia;

import java.sql.SQLException;
import java.util.List;

public interface SociaDao {
    int insertar(Socia socia) throws SQLException;
    Socia obtener(String dni) throws SQLException;
    List<Socia> listar(boolean incluirEliminadas) throws SQLException;
    int modificar(Socia socia) throws SQLException;
    int bajaLogica(String dni) throws SQLException;
    int reactivar(String dni) throws SQLException;
    int bajaFisica(String dni) throws SQLException;
}
