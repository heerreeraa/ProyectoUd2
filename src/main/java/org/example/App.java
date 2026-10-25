package org.example;

import org.example.menu.MenuPrincipal;
import org.example.seguridad.GestorUsuarios;
import org.example.utils.ConexionBD;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        try {
            ConexionBD.getInstance();
        } catch (SQLException e) {
            System.out.println("No se ha podido conectar con la base de datos. ¿Está arrancado el servidor (Docker)?");
            System.out.println("Detalle: " + e.getMessage());
            return;
        }

        if (!GestorUsuarios.login()) {
            ConexionBD.cerrar();
            return;
        }

        try {
            MenuPrincipal.mostrar();
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error inesperado: " + e.getMessage());
        }
        GestorUsuarios.registrarSesion(GestorUsuarios.getUsuarioActual(), "SALIDA");
        ConexionBD.cerrar();
    }
}
