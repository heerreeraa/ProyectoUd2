package org.example.export;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import org.example.estadisticas.EstadisticasAsociacion;
import org.example.estadisticas.HistoricoSocia;
import org.example.estadisticas.LineaHistorico;
import org.example.metadatos.InfoBBDD;
import org.example.metadatos.InfoColumna;
import org.example.metadatos.InfoTabla;
import org.example.metadatos.MetadatosCompletos;
import org.example.models.Inscripcion;
import org.example.models.Juego;
import org.example.models.Partida;
import org.example.models.Socia;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Exportador {

    private static final String CARPETA = "exportaciones";

    public static boolean exportarXML(Object datos, String nombre) {
        try {
            XStream xstream = new XStream();
            xstream.alias("socia", Socia.class);
            xstream.alias("juego", Juego.class);
            xstream.alias("partida", Partida.class);
            xstream.alias("inscripcion", Inscripcion.class);
            xstream.alias("bbdd", InfoBBDD.class);
            xstream.alias("tabla", InfoTabla.class);
            xstream.alias("columna", InfoColumna.class);
            xstream.alias("metadatos", MetadatosCompletos.class);
            xstream.alias("historico", HistoricoSocia.class);
            xstream.alias("linea", LineaHistorico.class);
            xstream.alias("estadisticas", EstadisticasAsociacion.class);
            if (datos instanceof List) {
                xstream.alias(nombre, List.class);
            }

            escribirFichero(xstream.toXML(datos), nombre + ".xml");
            System.out.println("Datos exportados a " + CARPETA + "/" + nombre + ".xml");
            return true;
        } catch (IOException e) {
            System.out.println("No se ha podido escribir el fichero XML: " + e.getMessage());
            return false;
        } catch (RuntimeException e) {
            System.out.println("No se han podido convertir los datos a XML: " + e.getMessage());
            return false;
        }
    }

    public static boolean exportarJSON(Object datos, String nombre) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            escribirFichero(gson.toJson(datos), nombre + ".json");
            System.out.println("Datos exportados a " + CARPETA + "/" + nombre + ".json");
            return true;
        } catch (IOException e) {
            System.out.println("No se ha podido escribir el fichero JSON: " + e.getMessage());
            return false;
        } catch (RuntimeException e) {
            System.out.println("No se han podido convertir los datos a JSON: " + e.getMessage());
            return false;
        }
    }

    private static void escribirFichero(String texto, String nombreFichero) throws IOException {
        File carpeta = new File(CARPETA);
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
        BufferedWriter escritor = new BufferedWriter(new FileWriter(new File(carpeta, nombreFichero), StandardCharsets.UTF_8));
        escritor.write(texto);
        escritor.close();
    }
}
