package org.example.menu;

import org.example.export.Exportador;
import org.example.utils.Teclado;

// Pregunta el formato y llama al Exportador; lo usan los menús de socias, juegos y partidas
public class MenuExportar {

    public static void exportar(Object datos, String nombre) {
        System.out.println("1. XML  2. JSON  3. Los dos  0. Cancelar");
        int formato = Teclado.leerEnteroRango("Formato: ", 0, 3);
        if (formato == 1 || formato == 3) {
            Exportador.exportarXML(datos, nombre);
        }
        if (formato == 2 || formato == 3) {
            Exportador.exportarJSON(datos, nombre);
        }
    }
}
