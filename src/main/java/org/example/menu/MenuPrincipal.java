package org.example.menu;

import org.example.dao.AsociacionDao;
import org.example.dao.AsociacionDaoImpl;
import org.example.models.Asociacion;
import org.example.utils.MensajesError;
import org.example.utils.Teclado;

import java.sql.SQLException;

public class MenuPrincipal {

    private static AsociacionDao asociacionDao = new AsociacionDaoImpl();

    public static void mostrar() {
        int opcion;
        do {
            mostrarCabecera();
            System.out.println("1. Gestión de socias");
            System.out.println("2. Gestión de juegos");
            System.out.println("0. Salir");
            opcion = Teclado.leerEntero("Elige una opción: ");

            switch (opcion) {
                case 1:
                    MenuSocias.mostrar();
                    break;
                case 2:
                    MenuJuegos.mostrar();
                    break;
                case 0:
                    System.out.println("¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    // Datos más relevantes de la asociación encima del menú
    private static void mostrarCabecera() {
        System.out.println("\n==========================================");
        try {
            Asociacion asociacion = asociacionDao.obtener();
            if (asociacion != null) {
                System.out.println(asociacion);
            } else {
                System.out.println("No hay datos de la asociación.");
            }
        } catch (SQLException e) {
            System.out.println(MensajesError.traducir(e));
        }
        System.out.println("==========================================");
    }
}
