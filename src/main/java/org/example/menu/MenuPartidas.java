package org.example.menu;

import org.example.dao.JuegoDao;
import org.example.dao.JuegoDaoImpl;
import org.example.dao.PartidaDao;
import org.example.dao.PartidaDaoImpl;
import org.example.dao.SociaDao;
import org.example.dao.SociaDaoImpl;
import org.example.models.Inscripcion;
import org.example.models.Juego;
import org.example.models.Partida;
import org.example.models.Socia;
import org.example.utils.MensajesError;
import org.example.utils.Teclado;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MenuPartidas {

    private static PartidaDao partidaDao = new PartidaDaoImpl();
    private static JuegoDao juegoDao = new JuegoDaoImpl();
    private static SociaDao sociaDao = new SociaDaoImpl();

    public static void mostrar() {
        int opcion;
        do {
            System.out.println("\n===== GESTIÓN DE PARTIDAS =====");
            System.out.println("1. Listar partidas");
            System.out.println("2. Listar todas (también las dadas de baja)");
            System.out.println("3. Ver una partida con sus jugadoras");
            System.out.println("4. Alta de partida");
            System.out.println("5. Modificar partida");
            System.out.println("6. Apuntar a una socia");
            System.out.println("7. Apuntar a una invitada");
            System.out.println("8. Quitar a una jugadora");
            System.out.println("9. Marcar ganadoras");
            System.out.println("10. Baja de partida");
            System.out.println("11. Reactivar partida dada de baja");
            System.out.println("12. Exportar partidas");
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
                        verPartida();
                        break;
                    case 4:
                        alta();
                        break;
                    case 5:
                        modificar();
                        break;
                    case 6:
                        apuntarSocia();
                        break;
                    case 7:
                        apuntarInvitada();
                        break;
                    case 8:
                        quitarJugadora();
                        break;
                    case 9:
                        marcarGanadoras();
                        break;
                    case 10:
                        baja();
                        break;
                    case 11:
                        reactivar();
                        break;
                    case 12:
                        MenuExportar.exportar(partidaDao.listar(false), "partidas");
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
        List<Partida> partidas = partidaDao.listar(incluirEliminadas);
        if (partidas.isEmpty()) {
            System.out.println("No hay partidas.");
            return;
        }
        for (Partida p : partidas) {
            System.out.println(p);
        }
        System.out.println("Total: " + partidas.size());
    }

    private static void verPartida() throws SQLException {
        Partida partida = pedirPartidaExistente();
        if (partida != null) {
            System.out.println(partida);
            mostrarJugadoras(partida);
        }
    }

    private static void alta() throws SQLException {
        Juego juego = pedirJuegoDisponible();
        if (juego == null) {
            return;
        }
        String fecha = Teclado.leerFecha("Fecha");
        String hora = Teclado.leerHora("Hora");
        // El número de jugadoras tiene que estar dentro del rango del juego
        int deseados = Teclado.leerEnteroRango("Jugadoras deseadas (" + juego.getMinJugadores() + "-"
                + juego.getMaxJugadores() + "): ", juego.getMinJugadores(), juego.getMaxJugadores());

        Partida partida = new Partida(juego.getId(), juego.getNombre(), fecha, hora, deseados, false);
        partidaDao.insertar(partida);
        System.out.println("Partida dada de alta.");
    }

    private static void modificar() throws SQLException {
        Partida partida = pedirPartidaExistente();
        if (partida == null) {
            return;
        }
        System.out.println(partida);
        Juego juego = juegoDao.obtener(partida.getIdJuego());
        if (Teclado.leerSiNo("¿Cambiar el juego?")) {
            Juego nuevo = pedirJuegoDisponible();
            if (nuevo != null && partida.getJugadoras().size() > nuevo.getMaxJugadores()) {
                System.out.println("Hay " + partida.getJugadoras().size() + " jugadoras apuntadas y " + nuevo.getNombre()
                        + " admite como máximo " + nuevo.getMaxJugadores() + ". Se mantiene " + juego.getNombre() + ".");
            } else if (nuevo != null) {
                juego = nuevo;
                partida.setIdJuego(juego.getId());
            }
        }
        System.out.println("Deja el campo vacío para no cambiarlo.");
        partida.setFecha(Teclado.leerFechaOpcional("Fecha", partida.getFecha()));
        partida.setHora(Teclado.leerHoraOpcional("Hora", partida.getHora()));

        // No puede haber menos plazas que jugadoras ya apuntadas
        int inscritas = partida.getJugadoras().size();
        int minimo = juego.getMinJugadores();
        if (inscritas > minimo) {
            minimo = inscritas;
        }
        if (minimo > juego.getMaxJugadores()) {
            System.out.println("Hay " + inscritas + " jugadoras apuntadas y " + juego.getNombre()
                    + " admite como máximo " + juego.getMaxJugadores() + ". Quita jugadoras antes de cambiar el juego.");
            return;
        }
        int deseados = partida.getJugadoresDeseados();
        if (deseados < minimo || deseados > juego.getMaxJugadores()) {
            deseados = Teclado.leerEnteroRango("Jugadoras deseadas (" + minimo + "-" + juego.getMaxJugadores() + "): ",
                    minimo, juego.getMaxJugadores());
        } else {
            deseados = Teclado.leerEnteroOpcional("Jugadoras deseadas (" + minimo + "-" + juego.getMaxJugadores() + ")",
                    deseados, minimo, juego.getMaxJugadores());
        }
        partida.setJugadoresDeseados(deseados);
        partidaDao.modificar(partida);
        System.out.println("Partida modificada.");
    }

    private static void apuntarSocia() throws SQLException {
        Partida partida = pedirPartidaAbierta();
        if (partida == null) {
            return;
        }
        String dni = Teclado.leerTexto("DNI/NIE/pasaporte de la socia: ").toUpperCase();
        Socia socia = sociaDao.obtener(dni);
        if (socia == null || socia.isEliminada()) {
            System.out.println("No hay ninguna socia activa con ese documento.");
            return;
        }
        for (Inscripcion i : partida.getJugadoras()) {
            if (dni.equals(i.getDniSocia())) {
                System.out.println(socia.getNombre() + " ya está apuntada a esta partida.");
                return;
            }
        }
        partidaDao.inscribirSocia(partida.getId(), dni);
        System.out.println(socia.getNombre() + " apuntada. Quedan " + (partida.getPlazasLibres() - 1) + " plazas libres.");
    }

    private static void apuntarInvitada() throws SQLException {
        Partida partida = pedirPartidaAbierta();
        if (partida == null) {
            return;
        }
        String nombre = Teclado.leerTexto("Nombre de la invitada: ");
        partidaDao.inscribirInvitada(partida.getId(), nombre);
        System.out.println(nombre + " apuntada. Quedan " + (partida.getPlazasLibres() - 1) + " plazas libres.");
    }

    private static void quitarJugadora() throws SQLException {
        Partida partida = pedirPartidaExistente();
        if (partida == null) {
            return;
        }
        Inscripcion inscripcion = elegirJugadora(partida);
        if (inscripcion != null) {
            partidaDao.desinscribir(inscripcion.getId());
            System.out.println("Jugadora quitada de la partida.");
        }
    }

    // Cada vez que se elige una jugadora se cambia entre ganadora y no ganadora
    private static void marcarGanadoras() throws SQLException {
        Partida partida = pedirPartidaExistente();
        if (partida == null) {
            return;
        }
        if (LocalDate.parse(partida.getFecha()).isAfter(LocalDate.now())) {
            System.out.println("Esta partida todavía no se ha jugado.");
            return;
        }
        Inscripcion inscripcion = elegirJugadora(partida);
        while (inscripcion != null) {
            boolean ganadora = !inscripcion.isGanadora();
            partidaDao.marcarGanadora(inscripcion.getId(), ganadora);
            inscripcion.setGanadora(ganadora);
            System.out.println(ganadora ? "Marcada como ganadora." : "Ya no está marcada como ganadora.");
            inscripcion = elegirJugadora(partida);
        }
    }

    private static void baja() throws SQLException {
        Partida partida = pedirPartidaExistente();
        if (partida == null) {
            return;
        }
        if (partida.isEliminada()) {
            System.out.println("Esta partida ya está dada de baja.");
            return;
        }
        System.out.println(partida);
        System.out.println("1. Baja lógica (deja de aparecer pero se conserva para las estadísticas)");
        System.out.println("2. Borrar definitivamente (también se borran sus inscripciones)");
        System.out.println("0. Cancelar");
        int tipo = Teclado.leerEnteroRango("Tipo de baja: ", 0, 2);

        if (tipo == 1) {
            partidaDao.bajaLogica(partida.getId());
            System.out.println("Partida dada de baja.");
        } else if (tipo == 2 && Teclado.leerSiNo("Se borrará para siempre con sus " + partida.getJugadoras().size()
                + " inscripciones. ¿Seguro?")) {
            partidaDao.bajaFisica(partida.getId());
            System.out.println("Partida borrada.");
        }
    }

    private static void reactivar() throws SQLException {
        Partida partida = pedirPartidaExistente();
        if (partida == null) {
            return;
        }
        if (!partida.isEliminada()) {
            System.out.println("Esta partida no está dada de baja.");
            return;
        }
        partidaDao.reactivar(partida.getId());
        System.out.println("Partida reactivada.");
    }

    // Muestra las jugadoras numeradas, con el nombre de la socia si lo es
    private static void mostrarJugadoras(Partida partida) throws SQLException {
        if (partida.getJugadoras().isEmpty()) {
            System.out.println("  No hay nadie apuntado.");
            return;
        }
        int numero = 1;
        for (Inscripcion i : partida.getJugadoras()) {
            String nombre;
            if (i.getDniSocia() != null) {
                Socia socia = sociaDao.obtener(i.getDniSocia());
                nombre = socia.getNombre() + " " + socia.getApellidos() + " (socia " + socia.getDni() + ")";
            } else {
                nombre = i.getNombreInvitada() + " (invitada)";
            }
            System.out.println("  " + numero + ". " + nombre + (i.isGanadora() ? " - GANADORA" : ""));
            numero++;
        }
    }

    // Devuelve la jugadora elegida o null si se elige 0
    private static Inscripcion elegirJugadora(Partida partida) throws SQLException {
        if (partida.getJugadoras().isEmpty()) {
            System.out.println("No hay nadie apuntado a esta partida.");
            return null;
        }
        mostrarJugadoras(partida);
        int numero = Teclado.leerEnteroRango("Número de la jugadora (0 para terminar): ", 0, partida.getJugadoras().size());
        if (numero == 0) {
            return null;
        }
        return partida.getJugadoras().get(numero - 1);
    }

    private static Partida pedirPartidaExistente() throws SQLException {
        int id = Teclado.leerEntero("Número de la partida: ");
        Partida partida = partidaDao.obtener(id);
        if (partida == null) {
            System.out.println("No existe ninguna partida con el número " + id + ".");
        }
        return partida;
    }

    // Una partida a la que todavía se puede apuntar gente
    private static Partida pedirPartidaAbierta() throws SQLException {
        Partida partida = pedirPartidaExistente();
        if (partida == null) {
            return null;
        }
        if (partida.isEliminada()) {
            System.out.println("Esta partida está dada de baja.");
            return null;
        }
        if (LocalDate.parse(partida.getFecha()).isBefore(LocalDate.now())) {
            System.out.println("Esta partida ya se ha jugado.");
            return null;
        }
        if (partida.getPlazasLibres() <= 0) {
            System.out.println("No quedan plazas libres en esta partida.");
            return null;
        }
        return partida;
    }

    // Un juego que existe, no está dado de baja y no está en reparación
    private static Juego pedirJuegoDisponible() throws SQLException {
        int id = Teclado.leerEntero("Número del juego: ");
        Juego juego = juegoDao.obtener(id);
        if (juego == null || juego.isEliminado()) {
            System.out.println("No hay ningún juego activo con el número " + id + ".");
            return null;
        }
        if (juego.getEstado().equals("EN_REPARACION")) {
            System.out.println(juego.getNombre() + " está en reparación y no se puede usar.");
            return null;
        }
        System.out.println("Juego: " + juego.getNombre() + " (" + juego.getMinJugadores() + "-" + juego.getMaxJugadores() + " jugadoras)");
        return juego;
    }
}
