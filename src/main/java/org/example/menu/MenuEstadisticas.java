package org.example.menu;

import org.example.estadisticas.EstadisticasAsociacion;
import org.example.estadisticas.EstadisticasDao;
import org.example.estadisticas.HistoricoSocia;
import org.example.estadisticas.LineaHistorico;
import org.example.utils.MensajesError;
import org.example.utils.Teclado;

import java.sql.SQLException;
import java.time.LocalDate;

public class MenuEstadisticas {

    private static EstadisticasDao estadisticasDao = new EstadisticasDao();

    public static void mostrar() {
        int opcion;
        do {
            System.out.println("\n===== ESTADÍSTICAS =====");
            System.out.println("1. Histórico de una socia");
            System.out.println("2. Estadísticas de la asociación");
            System.out.println("0. Volver");
            opcion = Teclado.leerEntero("Elige una opción: ");

            try {
                switch (opcion) {
                    case 1:
                        historicoSocia();
                        break;
                    case 2:
                        estadisticasAsociacion();
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

    private static void historicoSocia() throws SQLException {
        String dni = Teclado.leerTexto("DNI/NIE/pasaporte de la socia: ").toUpperCase();
        String desde = Teclado.leerFecha("Desde");
        String hasta = leerHasta(desde);

        HistoricoSocia historico = estadisticasDao.historicoSocia(dni, desde, hasta);
        if (historico == null) {
            System.out.println("No existe ninguna socia con el documento " + dni + ".");
            return;
        }
        System.out.println("\n" + historico);
        for (LineaHistorico linea : historico.getPartidas()) {
            System.out.println("  - " + linea);
        }
        if (Teclado.leerSiNo("¿Exportar los datos?")) {
            MenuExportar.exportar(historico, "historico_" + dni);
        }
    }

    private static void estadisticasAsociacion() throws SQLException {
        String desde = Teclado.leerFecha("Desde");
        String hasta = leerHasta(desde);

        EstadisticasAsociacion estadisticas = estadisticasDao.estadisticasAsociacion(desde, hasta);
        System.out.println("\n" + estadisticas);
        if (Teclado.leerSiNo("¿Exportar los datos?")) {
            MenuExportar.exportar(estadisticas, "estadisticas");
        }
    }

    private static String leerHasta(String desde) {
        String hasta = Teclado.leerFecha("Hasta");
        while (LocalDate.parse(hasta).isBefore(LocalDate.parse(desde))) {
            System.out.println("La fecha final no puede ser anterior a " + desde + ".");
            hasta = Teclado.leerFecha("Hasta");
        }
        return hasta;
    }
}
