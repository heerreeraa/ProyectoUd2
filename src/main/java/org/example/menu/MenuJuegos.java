package org.example.menu;

import org.example.dao.JuegoDao;
import org.example.dao.JuegoDaoImpl;
import org.example.dao.SociaDao;
import org.example.dao.SociaDaoImpl;
import org.example.models.Juego;
import org.example.models.Socia;
import org.example.utils.MensajesError;
import org.example.utils.Teclado;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MenuJuegos {

    private static JuegoDao juegoDao = new JuegoDaoImpl();
    private static SociaDao sociaDao = new SociaDaoImpl();

    // Límites razonables para no aceptar datos absurdos
    private static final int MAX_JUGADORES = 30;
    private static final int MAX_DURACION = 600;

    public static void mostrar() {
        int opcion;
        do {
            System.out.println("\n===== GESTIÓN DE JUEGOS =====");
            System.out.println("1. Listar juegos");
            System.out.println("2. Listar todos (también los dados de baja)");
            System.out.println("3. Ver un juego");
            System.out.println("4. Alta de juego");
            System.out.println("5. Modificar juego");
            System.out.println("6. Cambiar estado (disponible, prestado, en reparación)");
            System.out.println("7. Baja de juego");
            System.out.println("8. Reactivar juego dado de baja");
            System.out.println("0. Volver");
            opcion = Teclado.leerEntero("Elige una opción: ");

            try {
                switch (opcion) {
                    case 1:
                        listar(false);
                        break;
                    case 2:
                        listar(true);
                        break;
                    case 3:
                        verJuego();
                        break;
                    case 4:
                        alta();
                        break;
                    case 5:
                        modificar();
                        break;
                    case 6:
                        cambiarEstado();
                        break;
                    case 7:
                        baja();
                        break;
                    case 8:
                        reactivar();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (SQLException e) {
                System.out.println(MensajesError.traducir(e));
            }
        } while (opcion != 0);
    }

    private static void listar(boolean incluirEliminados) throws SQLException {
        List<Juego> juegos = juegoDao.listar(incluirEliminados);
        if (juegos.isEmpty()) {
            System.out.println("No hay juegos.");
            return;
        }
        for (Juego j : juegos) {
            System.out.println(j);
        }
        System.out.println("Total: " + juegos.size());
    }

    private static void verJuego() throws SQLException {
        Juego juego = pedirJuegoExistente();
        if (juego != null) {
            System.out.println(juego);
            System.out.println("    Descripción: " + juego.getDescripcion());
        }
    }

    private static void alta() throws SQLException {
        String nombre = Teclado.leerTexto("Nombre: ");
        String autores = Teclado.leerTexto("Autor o autores: ");
        String descripcion = Teclado.leerTextoOpcional("Descripción (opcional): ");
        int min = Teclado.leerEnteroRango("Mínimo de jugadoras: ", 1, MAX_JUGADORES);
        int max = Teclado.leerEnteroRango("Máximo de jugadoras: ", min, MAX_JUGADORES);
        int duracion = Teclado.leerEnteroRango("Duración estimada en minutos: ", 1, MAX_DURACION);
        String tematica = Teclado.leerTexto("Temática: ");
        String tipo = elegirTipo(null);
        String ubicacion = Teclado.leerTexto("Ubicación (por ejemplo Estantería A1): ");
        String dniPropietaria = elegirPropietaria(null);
        // La fecha de alta es la de hoy y el juego empieza disponible
        String fechaAlta = LocalDate.now().toString();

        Juego juego = new Juego(nombre, autores, descripcion, min, max, duracion, tematica, tipo, ubicacion,
                "DISPONIBLE", dniPropietaria, fechaAlta, false);
        juegoDao.insertar(juego);
        System.out.println("Juego dado de alta.");
    }

    private static void modificar() throws SQLException {
        Juego juego = pedirJuegoExistente();
        if (juego == null) {
            return;
        }
        System.out.println("Deja el campo vacío para no cambiarlo.");
        juego.setNombre(Teclado.leerTextoOpcional("Nombre", juego.getNombre()));
        juego.setAutores(Teclado.leerTextoOpcional("Autores", juego.getAutores()));
        juego.setDescripcion(Teclado.leerTextoOpcional("Descripción", juego.getDescripcion()));
        juego.setMinJugadores(Teclado.leerEnteroOpcional("Mínimo de jugadoras", juego.getMinJugadores(), 1, MAX_JUGADORES));
        juego.setMaxJugadores(Teclado.leerEnteroOpcional("Máximo de jugadoras", juego.getMaxJugadores(), 1, MAX_JUGADORES));
        // El máximo no puede quedar por debajo del mínimo
        while (juego.getMaxJugadores() < juego.getMinJugadores()) {
            System.out.println("El máximo no puede ser menor que el mínimo (" + juego.getMinJugadores() + ").");
            juego.setMaxJugadores(Teclado.leerEnteroRango("Máximo de jugadoras: ", juego.getMinJugadores(), MAX_JUGADORES));
        }
        juego.setDuracion(Teclado.leerEnteroOpcional("Duración (min)", juego.getDuracion(), 1, MAX_DURACION));
        juego.setTematica(Teclado.leerTextoOpcional("Temática", juego.getTematica()));
        juego.setTipo(elegirTipo(juego.getTipo()));
        juego.setUbicacion(Teclado.leerTextoOpcional("Ubicación", juego.getUbicacion()));
        juego.setDniPropietaria(elegirPropietaria(juego));
        juego.setFechaAlta(Teclado.leerFechaOpcional("Fecha de alta", juego.getFechaAlta()));
        juegoDao.modificar(juego);
        System.out.println("Juego modificado.");
    }

    private static void cambiarEstado() throws SQLException {
        Juego juego = pedirJuegoExistente();
        if (juego == null) {
            return;
        }
        System.out.println("Estado actual: " + juego.getEstado());
        System.out.println("1. Disponible  2. Prestado  3. En reparación");
        int opcion = Teclado.leerEnteroRango("Nuevo estado: ", 1, 3);
        String estado = "DISPONIBLE";
        if (opcion == 2) {
            estado = "PRESTADO";
        } else if (opcion == 3) {
            estado = "EN_REPARACION";
        }
        juegoDao.cambiarEstado(juego.getId(), estado);
        System.out.println("Estado cambiado a " + estado + ".");
    }

    private static void baja() throws SQLException {
        Juego juego = pedirJuegoExistente();
        if (juego == null) {
            return;
        }
        if (juego.isEliminado()) {
            System.out.println("Este juego ya está dado de baja.");
            return;
        }
        System.out.println(juego);
        System.out.println("1. Baja lógica (deja de aparecer pero se conservan sus partidas)");
        System.out.println("2. Borrar definitivamente");
        System.out.println("0. Cancelar");
        int tipo = Teclado.leerEnteroRango("Tipo de baja: ", 0, 2);

        if (tipo == 1) {
            juegoDao.bajaLogica(juego.getId());
            System.out.println("Juego dado de baja.");
        } else if (tipo == 2) {
            if (!Teclado.leerSiNo("Se borrará para siempre. ¿Seguro?")) {
                return;
            }
            try {
                juegoDao.bajaFisica(juego.getId());
                System.out.println("Juego borrado.");
            } catch (SQLException e) {
                // 1451: hay partidas de este juego
                if (e.getErrorCode() == 1451) {
                    System.out.println("No se puede borrar porque hay partidas de este juego.");
                    if (Teclado.leerSiNo("¿Quieres darlo de baja lógica?")) {
                        juegoDao.bajaLogica(juego.getId());
                        System.out.println("Juego dado de baja.");
                    }
                } else {
                    throw e;
                }
            }
        }
    }

    private static void reactivar() throws SQLException {
        Juego juego = pedirJuegoExistente();
        if (juego == null) {
            return;
        }
        if (!juego.isEliminado()) {
            System.out.println("Este juego no está dado de baja.");
            return;
        }
        juegoDao.reactivar(juego.getId());
        System.out.println("Juego reactivado.");
    }

    private static Juego pedirJuegoExistente() throws SQLException {
        int id = Teclado.leerEntero("Número del juego: ");
        Juego juego = juegoDao.obtener(id);
        if (juego == null) {
            System.out.println("No existe ningún juego con el número " + id + ".");
        }
        return juego;
    }

    // Si actual es null es obligatorio elegir; si no, con Enter se queda igual
    private static String elegirTipo(String actual) {
        while (true) {
            String texto;
            if (actual == null) {
                texto = Teclado.leerTexto("Tipo (1 = competitivo, 2 = cooperativo): ");
            } else {
                texto = Teclado.leerTextoOpcional("Tipo (1 = competitivo, 2 = cooperativo)", actual);
                if (texto.equals(actual)) {
                    return actual;
                }
            }
            if (texto.equals("1")) {
                return "COMPETITIVO";
            }
            if (texto.equals("2")) {
                return "COOPERATIVO";
            }
            System.out.println("Escribe 1 o 2.");
        }
    }

    // Devuelve el DNI de la socia propietaria, o null si el juego es de la asociación
    private static String elegirPropietaria(Juego juego) throws SQLException {
        if (juego != null) {
            String actual = juego.getDniPropietaria() == null ? "asociación" : juego.getDniPropietaria();
            System.out.println("Propietaria actual: " + actual);
            if (!Teclado.leerSiNo("¿Cambiar la propietaria?")) {
                return juego.getDniPropietaria();
            }
        }
        if (!Teclado.leerSiNo("¿El juego es de una socia? (si no, es de la asociación)")) {
            return null;
        }
        while (true) {
            String dni = Teclado.leerTexto("DNI de la socia propietaria: ").toUpperCase();
            Socia socia = sociaDao.obtener(dni);
            if (socia != null && !socia.isEliminada()) {
                return dni;
            }
            System.out.println("No hay ninguna socia activa con ese documento.");
            if (!Teclado.leerSiNo("¿Probar con otro DNI?")) {
                return null;
            }
        }
    }
}
