package org.example.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// Singleton: toda la aplicación comparte una única conexión con la BBDD
public class ConexionBD {

    private static ConexionBD instance;
    private Connection connection;

    // El constructor es privado para que nadie pueda hacer "new ConexionBD()"
    private ConexionBD() throws SQLException {
        // Valores por defecto por si no se puede leer config.properties
        String host = "localhost";
        String puerto = "3306";
        String bbdd = "asociacion_juegos";
        String usuario = "root";
        String password = "root";

        try {
            Properties config = new Properties();
            FileInputStream fichero = new FileInputStream("config.properties");
            config.load(fichero);
            fichero.close();
            host = config.getProperty("host", host);
            puerto = config.getProperty("puerto", puerto);
            bbdd = config.getProperty("bbdd", bbdd);
            usuario = config.getProperty("usuario", usuario);
            password = config.getProperty("password", password);
        } catch (IOException e) {
            System.out.println("No se ha podido leer config.properties, se usan los datos por defecto.");
        }

        String url = "jdbc:mariadb://" + host + ":" + puerto + "/" + bbdd;
        this.connection = DriverManager.getConnection(url, usuario, password);
    }

    public Connection getConnection() {
        return connection;
    }

    // Devuelve siempre la misma instancia; si la conexión ya no funciona, crea otra.
    // isValid(2) pregunta al servidor (máximo 2 segundos): así, si el servidor se ha caído
    // y vuelve a arrancar, la aplicación se reconecta sola
    public static ConexionBD getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConexionBD();
        } else if (!instance.getConnection().isValid(2)) {
            instance = new ConexionBD();
        }
        return instance;
    }

    // Se llama una sola vez al salir de la aplicación
    public static void cerrar() {
        try {
            if (instance != null && !instance.getConnection().isClosed()) {
                instance.getConnection().close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
