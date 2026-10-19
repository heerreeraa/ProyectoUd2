package org.example.export;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
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

// Exporta cualquier lista u objeto a exportaciones/<nombre>.xml o .json
public class Exportador {

    private static final String CARPETA = "exportaciones";

    public static boolean exportarXML(Object datos, String nombre) {
        try {
            XStream xstream = new XStream();
            // Los alias cambian el nombre completo de la clase (org.example.models.Juego) por uno corto (<juego>)
            xstream.alias("socia", Socia.class);
            xstream.alias("juego", Juego.class);
            xstream.alias("partida", Partida.class);
            xstream.alias("inscripcion", Inscripcion.class);
            xstream.alias("bbdd", InfoBBDD.class);
            xstream.alias("tabla", InfoTabla.class);
            xstream.alias("columna", InfoColumna.class);
            xstream.alias("metadatos", MetadatosCompletos.class);
            // XStream trata cualquier ArrayList como List, así que el alias de la raíz va sobre List
            if (datos instanceof List) {
                xstream.alias(nombre, List.class);
            } else {
                xstream.alias(nombre, datos.getClass());
            }

            escribirFichero(xstream.toXML(datos), nombre + ".xml");
            System.out.println("Datos exportados a " + CARPETA + "/" + nombre + ".xml");
            return true;
        } catch (IOException e) {
            System.out.println("No se ha podido escribir el fichero XML: " + e.getMessage());
            return false;
        } catch (RuntimeException e) {
            // XStream lanza excepciones propias si no puede convertir los datos
            System.out.println("No se han podido convertir los datos a XML: " + e.getMessage());
            return false;
        }
    }

    public static boolean exportarJSON(Object datos, String nombre) {
        try {
            // setPrettyPrinting pone saltos de línea y sangría para que el JSON se pueda leer
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

    // Crea la carpeta si no existe y escribe el texto en el fichero
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
