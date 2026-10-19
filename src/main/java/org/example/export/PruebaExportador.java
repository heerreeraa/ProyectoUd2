package org.example.export;

import org.example.dao.JuegoDaoImpl;
import org.example.models.Juego;
import org.example.utils.MensajesError;

import java.sql.SQLException;
import java.util.List;

public class PruebaExportador {

    public static void main(String[] args) {
        try {
            List<Juego> juegos = new JuegoDaoImpl().listar(false);
            Exportador.exportarXML(juegos, "juegos");
            Exportador.exportarJSON(juegos, "juegos");
        } catch (SQLException e) {
            System.out.println(MensajesError.traducir(e));
        }
    }
}
