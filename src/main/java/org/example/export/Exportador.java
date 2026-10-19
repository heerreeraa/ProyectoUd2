package org.example.export;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import org.example.models.Inscripcion;
import org.example.models.Juego;
import org.example.models.Partida;
import org.example.models.Socia;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Exportador {

    public static boolean exportarXML(Object datos, String nombre) {
        if (datos == null) {
            System.out.println("No se pueden exportar datos vacíos a XML.");
            return false;
        }

        BufferedWriter escritor = null;
        try {
            File archivo = crearArchivo(nombre, "xml");

            XStream xstream = new XStream();
            xstream.alias("socia", Socia.class);
            xstream.alias("juego", Juego.class);
            xstream.alias("partida", Partida.class);
            xstream.alias("inscripcion", Inscripcion.class);
            xstream.alias(nombre, datos.getClass());

            escritor = new BufferedWriter(new FileWriter(archivo, StandardCharsets.UTF_8));
            escritor.write(xstream.toXML(datos));
            escritor.close();
            escritor = null;

            System.out.println("Datos exportados a exportaciones/" + nombre + ".xml");
            return true;
        } catch (IOException e) {
            System.out.println("No se pudo escribir el fichero XML: " + e.getMessage());
            return false;
        } catch (XStreamException e) {
            System.out.println("No se pudieron convertir los datos a XML: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo crear el fichero XML: " + e.getMessage());
            return false;
        } finally {
            if (escritor != null) {
                try {
                    escritor.close();
                } catch (IOException e) {
                    System.out.println("No se pudo cerrar el fichero XML: " + e.getMessage());
                }
            }
        }
    }

    public static boolean exportarJSON(Object datos, String nombre) {
        BufferedWriter escritor = null;
        try {
            File archivo = crearArchivo(nombre, "json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            escritor = new BufferedWriter(new FileWriter(archivo, StandardCharsets.UTF_8));
            gson.toJson(datos, escritor);
            escritor.close();
            escritor = null;

            System.out.println("Datos exportados a exportaciones/" + nombre + ".json");
            return true;
        } catch (IOException e) {
            System.out.println("No se pudo escribir el fichero JSON: " + e.getMessage());
            return false;
        } catch (JsonIOException | IllegalArgumentException e) {
            System.out.println("No se pudieron convertir los datos a JSON: " + e.getMessage());
            return false;
        } finally {
            if (escritor != null) {
                try {
                    escritor.close();
                } catch (IOException e) {
                    System.out.println("No se pudo cerrar el fichero JSON: " + e.getMessage());
                }
            }
        }
    }

    private static File crearArchivo(String nombre, String extension) throws IOException {
        if (nombre == null || nombre.trim().isEmpty()
                || nombre.contains("/") || nombre.contains("\\")
                || nombre.equals(".") || nombre.equals("..")) {
            throw new IllegalArgumentException("El nombre del fichero no es válido.");
        }

        File carpeta = new File("exportaciones");
        if (!carpeta.exists() && !carpeta.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta exportaciones.");
        }
        if (!carpeta.isDirectory()) {
            throw new IOException("La ruta exportaciones no es una carpeta.");
        }
        return new File(carpeta, nombre + "." + extension);
    }
}
