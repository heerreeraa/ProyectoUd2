package org.example.utils;

import java.sql.SQLException;

public class MensajesError {

    public static String traducir(SQLException e) {
        switch (e.getErrorCode()) {
            case 1062:
                return "Ya existe un registro con esa clave.";
            case 1451:
                return "No se puede borrar porque tiene otros datos relacionados (juegos, partidas o inscripciones).";
            case 1452:
                return "Hace referencia a un dato que no existe.";
            case 4025:
                return "Los datos no cumplen las reglas de la base de datos (por ejemplo, mínimo de jugadoras mayor que el máximo).";
        }
        if (e.getSQLState() != null && e.getSQLState().startsWith("08")) {
            return "Se ha perdido la conexión con la base de datos. Comprueba que el servidor está arrancado.";
        }
        return "Error en la base de datos: " + e.getMessage();
    }
}
