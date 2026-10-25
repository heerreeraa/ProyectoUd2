package org.example.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {

    private static ConexionBD instance;
    private Connection connection;

    private ConexionBD() throws SQLException {
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

    public static ConexionBD getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConexionBD();
        } else if (!instance.getConnection().isValid(2)) {
            instance = new ConexionBD();
        }
        return instance;
    }

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
