package org.example;

import org.example.dao.JuegoDao;
import org.example.dao.JuegoDaoImpl;
import org.example.models.Juego;
import org.example.utils.ConexionBD;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class JuegoDaoTest {

    private JuegoDao juegoDao = new JuegoDaoImpl();
    private int idPrueba;

    @BeforeEach
    public void crearJuegoDePrueba() throws SQLException {
        juegoDao.insertar(new Juego("Juego JUnit", "Autora Prueba", "Juego para los tests", 2, 4, 30,
                "Pruebas", "COOPERATIVO", "Estantería Z9", "DISPONIBLE", null, "2026-10-19", false));
        idPrueba = ultimoIdJuego();
    }

    @AfterEach
    public void borrarJuegoDePrueba() throws SQLException {
        juegoDao.bajaFisica(idPrueba);
    }

    @Test
    public void insertarYObtener() throws SQLException {
        Juego juego = juegoDao.obtener(idPrueba);
        assertEquals("Juego JUnit", juego.getNombre());
        assertEquals(2, juego.getMinJugadores());
        assertNull(juego.getDniPropietaria());
    }

    @Test
    public void cambiarEstado() throws SQLException {
        juegoDao.cambiarEstado(idPrueba, "PRESTADO");
        assertEquals("PRESTADO", juegoDao.obtener(idPrueba).getEstado());
    }

    @Test
    public void buscarPorTipoYJugadoras() throws SQLException {
        List<Juego> juegos = juegoDao.buscar("JUnit", "", "COOPERATIVO", 3, -1, "");
        assertEquals(1, juegos.size());

        List<Juego> ninguno = juegoDao.buscar("JUnit", "", "", 6, -1, "");
        assertTrue(ninguno.isEmpty());
    }

    @Test
    public void minimoMayorQueMaximoNoSeGuarda() throws SQLException {
        Juego juego = juegoDao.obtener(idPrueba);
        juego.setMinJugadores(5);
        try {
            juegoDao.modificar(juego);
            fail("Se ha guardado un juego con mínimo 5 y máximo 4");
        } catch (SQLException e) {
            assertEquals(4025, e.getErrorCode());
        }
    }

    @Test
    public void noSePuedeBorrarUnJuegoConPartidas() {
        try {
            juegoDao.bajaFisica(1);
            fail("Se ha podido borrar un juego con partidas");
        } catch (SQLException e) {
            assertEquals(1451, e.getErrorCode());
        }
    }

    private int ultimoIdJuego() throws SQLException {
        PreparedStatement ps = ConexionBD.getInstance().getConnection().prepareStatement("select max(id) from juego");
        ResultSet rs = ps.executeQuery();
        rs.next();
        int id = rs.getInt(1);
        rs.close();
        ps.close();
        return id;
    }
}
