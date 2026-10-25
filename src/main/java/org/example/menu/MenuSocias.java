package org.example.menu;

import org.example.dao.JuegoDao;
import org.example.dao.JuegoDaoImpl;
import org.example.dao.PartidaDao;
import org.example.dao.PartidaDaoImpl;
import org.example.dao.SociaDao;
import org.example.dao.SociaDaoImpl;
import org.example.models.Juego;
import org.example.models.Partida;
import org.example.models.Socia;
import org.example.utils.MensajesError;
import org.example.utils.Teclado;
import org.example.utils.Validador;

import java.sql.SQLException;
import java.util.List;

public class MenuSocias {

    private static SociaDao sociaDao = new SociaDaoImpl();
    private static JuegoDao juegoDao = new JuegoDaoImpl();
    private static PartidaDao partidaDao = new PartidaDaoImpl();

    public static void mostrar() {
        int opcion;
        do {
            System.out.println("\n===== GESTIÓN DE SOCIAS =====");
            System.out.println("1. Listar socias");
            System.out.println("2. Listar todas (también las dadas de baja)");
            System.out.println("3. Ver ficha de una socia");
            System.out.println("4. Alta de socia");
            System.out.println("5. Modificar socia");
            System.out.println("6. Baja de socia");
            System.out.println("7. Reactivar socia dada de baja");
            System.out.println("8. Exportar socias");
            System.out.println("9. Buscar socias");
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
                        verFicha();
                        break;
                    case 4:
                        alta();
                        break;
                    case 5:
                        modificar();
                        break;
                    case 6:
                        baja();
                        break;
                    case 7:
                        reactivar();
                        break;
                    case 8:
                        MenuExportar.exportar(sociaDao.listar(false), "socias");
                        break;
                    case 9:
                        buscar();
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

    private static void listar(boolean incluirEliminadas) throws SQLException {
        List<Socia> socias = sociaDao.listar(incluirEliminadas);
        if (socias.isEmpty()) {
            System.out.println("No hay socias.");
            return;
        }
        for (Socia s : socias) {
            System.out.println(s);
        }
        System.out.println("Total: " + socias.size());
    }

    private static void buscar() throws SQLException {
        System.out.println("Rellena solo los criterios que quieras usar (Enter para saltar).");
        String nombre = Teclado.leerTextoOpcional("Nombre (o parte): ");
        String apellidos = Teclado.leerTextoOpcional("Apellidos (o parte): ");
        int edadMinima = Teclado.leerEnteroOVacio("Edad mínima: ", 0, 120);
        int edadMaxima = Teclado.leerEnteroOVacio("Edad máxima: ", 0, 120);

        List<Socia> socias = sociaDao.buscar(nombre, apellidos, edadMinima, edadMaxima);
        if (socias.isEmpty()) {
            System.out.println("No hay ninguna socia con esos criterios.");
            return;
        }
        for (Socia s : socias) {
            System.out.println(s);
        }
        System.out.println("Encontradas: " + socias.size());
    }

    private static void verFicha() throws SQLException {
        Socia socia = pedirSociaExistente();
        if (socia == null) {
            return;
        }
        System.out.println("\n" + socia);
        List<Juego> juegos = juegoDao.listarPorPropietaria(socia.getDni());
        System.out.println("Juegos que ha traído a la asociación: " + juegos.size());
        for (Juego j : juegos) {
            System.out.println("  - [" + j.getId() + "] " + j.getNombre() + " (" + j.getEstado() + ")");
        }
        List<Partida> partidas = partidaDao.listarPorSocia(socia.getDni());
        System.out.println("Partidas en las que está inscrita: " + partidas.size());
        for (Partida p : partidas) {
            System.out.println("  - [" + p.getId() + "] " + p.getFecha() + " a las " + p.getHora() + " | " + p.getNombreJuego());
        }
    }

    private static void alta() throws SQLException {
        String dni = leerDocumento();
        Socia existente = sociaDao.obtener(dni);
        if (existente != null) {
            if (existente.isEliminada()) {
                System.out.println("Esa socia ya existe pero está dada de baja. Puedes reactivarla con la opción 7.");
            } else {
                System.out.println("Ya existe una socia con ese documento.");
            }
            return;
        }
        String nombre = Teclado.leerTexto("Nombre: ");
        String apellidos = Teclado.leerTexto("Apellidos: ");
        String telefono = leerTelefono(null);
        int edad = Teclado.leerEnteroRango("Edad: ", 0, 120);
        String email = leerEmail(null);

        Socia socia = new Socia(dni, nombre, apellidos, telefono, edad, email, false);
        sociaDao.insertar(socia);
        System.out.println("Socia dada de alta.");
    }

    private static void modificar() throws SQLException {
        Socia socia = pedirSociaExistente();
        if (socia == null) {
            return;
        }
        System.out.println("Deja el campo vacío para no cambiarlo.");
        socia.setNombre(Teclado.leerTextoOpcional("Nombre", socia.getNombre()));
        socia.setApellidos(Teclado.leerTextoOpcional("Apellidos", socia.getApellidos()));
        socia.setTelefono(leerTelefono(socia.getTelefono()));
        socia.setEdad(Teclado.leerEnteroOpcional("Edad", socia.getEdad(), 0, 120));
        socia.setEmail(leerEmail(socia.getEmail()));
        sociaDao.modificar(socia);
        System.out.println("Socia modificada.");
    }

    private static void baja() throws SQLException {
        Socia socia = pedirSociaExistente();
        if (socia == null) {
            return;
        }
        if (socia.isEliminada()) {
            System.out.println("Esta socia ya está dada de baja.");
            return;
        }
        System.out.println(socia);
        System.out.println("1. Baja lógica (deja de aparecer pero se conserva su historial)");
        System.out.println("2. Borrar definitivamente");
        System.out.println("0. Cancelar");
        int tipo = Teclado.leerEnteroRango("Tipo de baja: ", 0, 2);

        if (tipo == 1) {
            sociaDao.bajaLogica(socia.getDni());
            System.out.println("Socia dada de baja.");
        } else if (tipo == 2) {
            if (!Teclado.leerSiNo("Se borrará para siempre. ¿Seguro?")) {
                return;
            }
            try {
                sociaDao.bajaFisica(socia.getDni());
                System.out.println("Socia borrada.");
            } catch (SQLException e) {
                if (e.getErrorCode() == 1451) {
                    System.out.println("No se puede borrar porque tiene juegos o ha participado en partidas.");
                    if (Teclado.leerSiNo("¿Quieres darla de baja lógica?")) {
                        sociaDao.bajaLogica(socia.getDni());
                        System.out.println("Socia dada de baja.");
                    }
                } else {
                    throw e;
                }
            }
        }
    }

    private static void reactivar() throws SQLException {
        Socia socia = pedirSociaExistente();
        if (socia == null) {
            return;
        }
        if (!socia.isEliminada()) {
            System.out.println("Esta socia no está dada de baja.");
            return;
        }
        sociaDao.reactivar(socia.getDni());
        System.out.println("Socia reactivada.");
    }

    private static Socia pedirSociaExistente() throws SQLException {
        String dni = Teclado.leerTexto("DNI/NIE/pasaporte: ").toUpperCase();
        Socia socia = sociaDao.obtener(dni);
        if (socia == null) {
            System.out.println("No existe ninguna socia con el documento " + dni + ".");
        }
        return socia;
    }

    private static String leerDocumento() {
        while (true) {
            String dni = Teclado.leerTexto("DNI/NIE/pasaporte: ").toUpperCase();
            if (Validador.esDocumentoValido(dni)) {
                return dni;
            }
            System.out.println("Documento no válido. Ejemplos: 12345678Z, X1234567L o un pasaporte de 6 a 15 caracteres.");
        }
    }

    private static String leerTelefono(String actual) {
        while (true) {
            String telefono;
            if (actual == null) {
                telefono = Teclado.leerTexto("Teléfono: ");
            } else {
                telefono = Teclado.leerTextoOpcional("Teléfono", actual);
            }
            if (Validador.esTelefonoValido(telefono)) {
                return telefono;
            }
            System.out.println("Teléfono no válido. Escribe entre 9 y 15 cifras.");
        }
    }

    private static String leerEmail(String actual) {
        while (true) {
            String email;
            if (actual == null) {
                email = Teclado.leerTexto("Email: ");
            } else {
                email = Teclado.leerTextoOpcional("Email", actual);
            }
            if (Validador.esEmailValido(email)) {
                return email;
            }
            System.out.println("Email no válido. Ejemplo: nombre@mail.com");
        }
    }
}
