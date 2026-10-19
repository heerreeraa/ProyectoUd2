package org.example.menu;

import org.example.export.Exportador;
import org.example.metadatos.InfoColumna;
import org.example.metadatos.InfoTabla;
import org.example.metadatos.Metadatos;
import org.example.metadatos.MetadatosCompletos;
import org.example.utils.MensajesError;
import org.example.utils.Teclado;

import java.sql.SQLException;
import java.util.ArrayList;

public class MenuMetadatos {

    public static void mostrar() {
        int opcion;
        do {
            System.out.println("\n===== METADATOS DE LA BASE DE DATOS =====");
            System.out.println("1. Ver datos de la base de datos");
            System.out.println("2. Ver tablas (clave primaria y columnas)");
            System.out.println("3. Ver columnas de una tabla");
            System.out.println("4. Exportar metadatos a XML");
            System.out.println("5. Exportar metadatos a JSON");
            System.out.println("0. Volver");
            opcion = Teclado.leerEntero("Elige una opción: ");

            try {
                switch (opcion) {
                    case 1:
                        System.out.println(Metadatos.obtenerInfoBBDD());
                        break;
                    case 2:
                        verTablas();
                        break;
                    case 3:
                        verColumnas();
                        break;
                    case 4:
                        Exportador.exportarXML(obtenerTodo(), "metadatos");
                        break;
                    case 5:
                        Exportador.exportarJSON(obtenerTodo(), "metadatos");
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

    private static void verTablas() throws SQLException {
        for (InfoTabla tabla : Metadatos.obtenerTablas()) {
            System.out.println("\nEsquema: " + tabla.getEsquema() + " | Tabla: " + tabla.getNombre()
                    + " | Clave primaria: " + tabla.getClavePrimaria());
            for (InfoColumna columna : tabla.getColumnas()) {
                System.out.println("  - " + columna);
            }
        }
    }

    private static void verColumnas() throws SQLException {
        String tabla = Teclado.leerTexto("Nombre de la tabla: ").toLowerCase();
        ArrayList<InfoColumna> columnas = Metadatos.obtenerColumnas(tabla);
        // Si la tabla no existe, getColumns no da error: devuelve una lista vacía
        if (columnas.isEmpty()) {
            System.out.println("No existe ninguna tabla llamada " + tabla + ".");
            return;
        }
        for (InfoColumna columna : columnas) {
            System.out.println("  - " + columna);
        }
    }

    private static MetadatosCompletos obtenerTodo() throws SQLException {
        return new MetadatosCompletos(Metadatos.obtenerInfoBBDD(), Metadatos.obtenerTablas());
    }
}
