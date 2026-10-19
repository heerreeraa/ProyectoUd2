package org.example.dao;

import org.example.models.Asociacion;

import java.sql.SQLException;

public interface AsociacionDao {
    Asociacion obtener() throws SQLException;
    int modificar(Asociacion asociacion) throws SQLException;
}
