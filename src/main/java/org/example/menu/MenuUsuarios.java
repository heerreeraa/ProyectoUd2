package org.example.menu;

import org.example.seguridad.GestorUsuarios;
import org.example.utils.Teclado;

public class MenuUsuarios {

    public static void mostrar() {
        int opcion;
        do {
            System.out.println("\n===== GESTIÓN DE USUARIOS =====");
            System.out.println("1. Crear usuario");
            System.out.println("2. Cambiar mi contraseña");
            System.out.println("3. Ver registro de sesiones");
            System.out.println("0. Volver");
            opcion = Teclado.leerEntero("Elige una opción: ");

            switch (opcion) {
                case 1:
                    GestorUsuarios.crearUsuario();
                    break;
                case 2:
                    GestorUsuarios.cambiarPassword();
                    break;
                case 3:
                    GestorUsuarios.verSesiones();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }
}
