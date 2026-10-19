package org.example;

import org.example.utils.ConexionBD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        // De momento solo comprobamos que la conexión funciona.
        // El menú principal se añade en la siguiente fase.
        try {
            PreparedStatement ps = ConexionBD.getInstance().getConnection()
                    .prepareStatement("select razon_social from asociacion where id = 1");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Conexión correcta con la BBDD de: " + rs.getString("razon_social"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("No se ha podido conectar con la base de datos. ¿Está arrancado Docker?");
            System.out.println("Detalle: " + e.getMessage());
        }
        ConexionBD.cerrar();
    }
}
