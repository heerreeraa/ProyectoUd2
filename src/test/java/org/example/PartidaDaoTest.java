package org.example;

import org.example.dao.PartidaDao;
import org.example.dao.PartidaDaoImpl;
import org.example.models.Partida;
import org.example.utils.ConexionBD;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

// Pruebas del DAO de partidas: inscripciones, plazas libres, ganadoras y borrado en cascada
public class PartidaDaoTest {

    private PartidaDao partidaDao = new PartidaDaoImpl();
    private int idPrueba;

    // Partida de Hanabi (id 8) para 3 jugadoras
    @BeforeEach
    public void crearPartidaDePrueba() throws SQLException {
        partidaDao.insertar(new Partida(8, "Hanabi", "2026-12-01", "18:00", 3, false));
        idPrueba = ultimoIdPartida();
    }

    @AfterEach
    public void borrarPartidaDePrueba() throws SQLException {
        partidaDao.bajaFisica(idPrueba);
    }

    @Test
    public void obtenerConNombreDelJuego() throws SQLException {
        Partida partida = partidaDao.obtener(idPrueba);
        assertEquals("Hanabi", partida.getNombreJuego());
        assertEquals("18:00", partida.getHora());
        assertEquals(3, partida.getPlazasLibres());
    }

    @Test
    public void inscribirReducePlazasLibres() throws SQLException {
        partidaDao.inscribirSocia(idPrueba, "12345678Z");
        partidaDao.inscribirInvitada(idPrueba, "Invitada JUnit");
        Partida partida = partidaDao.obtener(idPrueba);
        assertEquals(2, partida.getJugadoras().size());
        assertEquals(1, partida.getPlazasLibres());
    }

    // El UNIQUE (id_partida, dni_socia) no deja apuntar dos veces a la misma socia
    @Test
    public void noSePuedeInscribirDosVeces() throws SQLException {
        partidaDao.inscribirSocia(idPrueba, "12345678Z");
        try {
            partidaDao.inscribirSocia(idPrueba, "12345678Z");
            fail("Se ha podido apuntar dos veces a la misma socia");
        } catch (SQLException e) {
            assertEquals(1062, e.getErrorCode());
        }
    }

    @Test
    public void marcarGanadora() throws SQLException {
        partidaDao.inscribirSocia(idPrueba, "23456789D");
        int idInscripcion = partidaDao.obtener(idPrueba).getJugadoras().get(0).getId();
        partidaDao.marcarGanadora(idInscripcion, true);
        assertTrue(partidaDao.obtener(idPrueba).getJugadoras().get(0).isGanadora());
    }

    // Al borrar la partida, el ON DELETE CASCADE borra también sus inscripciones
    @Test
    public void borrarPartidaBorraSusInscripciones() throws SQLException {
        partidaDao.inscribirSocia(idPrueba, "12345678Z");
        int idInscripcion = partidaDao.obtener(idPrueba).getJugadoras().get(0).getId();

        partidaDao.bajaFisica(idPrueba);
        assertNull(partidaDao.obtener(idPrueba));
        assertEquals(0, contarInscripcion(idInscripcion));
    }

    @Test
    public void buscarConPlazasLibres() throws SQLException {
        assertEquals(1, partidaDao.buscar("Hanabi", "2026-12-01", "2026-12-01", true).size());
    }

    private int ultimoIdPartida() throws SQLException {
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement("select max(id) from partida");
        ResultSet rs = ps.executeQuery();
        rs.next();
        int id = rs.getInt(1);
        rs.close();
        ps.close();
        return id;
    }

    private int contarInscripcion(int idInscripcion) throws SQLException {
        PreparedStatement ps = ConexionBD.getInstance().getConnection()
                .prepareStatement("select count(*) from partida_jugadora where id = ?");
        ps.setInt(1, idInscripcion);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int total = rs.getInt(1);
        rs.close();
        ps.close();
        return total;
    }
}
