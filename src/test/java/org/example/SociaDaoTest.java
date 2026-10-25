package org.example;

import org.example.dao.SociaDao;
import org.example.dao.SociaDaoImpl;
import org.example.models.Socia;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SociaDaoTest {

    private static final String DNI_PRUEBA = "00000000T";
    private SociaDao sociaDao = new SociaDaoImpl();

    @BeforeEach
    public void crearSociaDePrueba() throws SQLException {
        sociaDao.insertar(new Socia(DNI_PRUEBA, "Prueba", "JUnit Test", "600000000", 30, "prueba@mail.com", false));
    }

    @AfterEach
    public void borrarSociaDePrueba() throws SQLException {
        sociaDao.bajaFisica(DNI_PRUEBA);
    }

    @Test
    public void insertarYObtener() throws SQLException {
        Socia socia = sociaDao.obtener(DNI_PRUEBA);
        assertNotNull(socia);
        assertEquals("Prueba", socia.getNombre());
        assertEquals(30, socia.getEdad());
    }

    @Test
    public void obtenerUnaQueNoExiste() throws SQLException {
        assertNull(sociaDao.obtener("99999999R"));
    }

    @Test
    public void modificar() throws SQLException {
        Socia socia = sociaDao.obtener(DNI_PRUEBA);
        socia.setEdad(31);
        socia.setEmail("nuevo@mail.com");
        sociaDao.modificar(socia);

        Socia modificada = sociaDao.obtener(DNI_PRUEBA);
        assertEquals(31, modificada.getEdad());
        assertEquals("nuevo@mail.com", modificada.getEmail());
    }

    @Test
    public void bajaLogicaYReactivar() throws SQLException {
        sociaDao.bajaLogica(DNI_PRUEBA);
        assertTrue(sociaDao.obtener(DNI_PRUEBA).isEliminada());
        assertFalse(contiene(sociaDao.listar(false), DNI_PRUEBA));
        assertTrue(contiene(sociaDao.listar(true), DNI_PRUEBA));

        sociaDao.reactivar(DNI_PRUEBA);
        assertFalse(sociaDao.obtener(DNI_PRUEBA).isEliminada());
    }

    @Test
    public void noSePuedeInsertarDosVeces() {
        try {
            sociaDao.insertar(new Socia(DNI_PRUEBA, "Otra", "Igual", "600000001", 20, "otra@mail.com", false));
            fail("Se ha podido insertar dos veces el mismo DNI");
        } catch (SQLException e) {
            assertEquals(1062, e.getErrorCode());
        }
    }

    @Test
    public void noSePuedeBorrarUnaSociaConPartidas() {
        try {
            sociaDao.bajaFisica("12345678Z");
            fail("Se ha podido borrar una socia con partidas");
        } catch (SQLException e) {
            assertEquals(1451, e.getErrorCode());
        }
    }

    @Test
    public void buscarPorNombreYEdad() throws SQLException {
        List<Socia> socias = sociaDao.buscar("Prueb", "", 25, 35);
        assertTrue(contiene(socias, DNI_PRUEBA));

        List<Socia> ninguna = sociaDao.buscar("Prueb", "", 40, -1);
        assertFalse(contiene(ninguna, DNI_PRUEBA));
    }

    private boolean contiene(List<Socia> socias, String dni) {
        for (Socia s : socias) {
            if (s.getDni().equals(dni)) {
                return true;
            }
        }
        return false;
    }
}
