package org.example.dao;

import org.example.models.Partida;

import java.sql.SQLException;
import java.util.List;

public interface PartidaDao {
    int insertar(Partida partida) throws SQLException;
    Partida obtener(int id) throws SQLException;
    List<Partida> listar(boolean incluirEliminadas) throws SQLException;
    List<Partida> listarPorSocia(String dni) throws SQLException;
    List<Partida> buscar(String juego, String desde, String hasta, boolean soloConPlazas) throws SQLException;
    int modificar(Partida partida) throws SQLException;
    int bajaLogica(int id) throws SQLException;
    int reactivar(int id) throws SQLException;
    int bajaFisica(int id) throws SQLException;
    int inscribirSocia(int idPartida, String dni) throws SQLException;
    int inscribirInvitada(int idPartida, String nombre) throws SQLException;
    int desinscribir(int idInscripcion) throws SQLException;
    int marcarGanadora(int idInscripcion, boolean ganadora) throws SQLException;
}
