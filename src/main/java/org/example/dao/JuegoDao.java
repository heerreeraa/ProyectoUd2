package org.example.dao;

import org.example.models.Juego;

import java.sql.SQLException;
import java.util.List;

public interface JuegoDao {
    int insertar(Juego juego) throws SQLException;
    Juego obtener(int id) throws SQLException;
    List<Juego> listar(boolean incluirEliminados) throws SQLException;
    List<Juego> listarPorPropietaria(String dni) throws SQLException;
    List<Juego> buscar(String nombre, String tematica, String tipo, int jugadoras, int duracionMaxima, String estado) throws SQLException;
    int modificar(Juego juego) throws SQLException;
    int cambiarEstado(int id, String estado) throws SQLException;
    int bajaLogica(int id) throws SQLException;
    int reactivar(int id) throws SQLException;
    int bajaFisica(int id) throws SQLException;
}
