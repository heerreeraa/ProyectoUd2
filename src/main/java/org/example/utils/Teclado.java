package org.example.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

// Lectura de teclado que nunca lanza excepciones: si el dato no vale, se vuelve a pedir
public class Teclado {

    // Un único Scanner para toda la aplicación
    private static Scanner sc = new Scanner(System.in);

    // Lee una línea; si se cierra la entrada (Ctrl+D) se sale de la aplicación
    private static String leerLinea(String mensaje) {
        System.out.print(mensaje);
        if (!sc.hasNextLine()) {
            System.out.println("\nSe ha cerrado la entrada de teclado. Saliendo...");
            ConexionBD.cerrar();
            System.exit(0);
        }
        return sc.nextLine().trim();
    }

    // Texto obligatorio (no admite vacío)
    public static String leerTexto(String mensaje) {
        String texto = leerLinea(mensaje);
        while (texto.isEmpty()) {
            System.out.println("Este campo no puede estar vacío.");
            texto = leerLinea(mensaje);
        }
        return texto;
    }

    // Texto que puede dejarse vacío (para "dejar en blanco si no quieres cambiarlo")
    public static String leerTextoOpcional(String mensaje) {
        return leerLinea(mensaje);
    }

    public static int leerEntero(String mensaje) {
        while (true) {
            String texto = leerLinea(mensaje);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Tienes que escribir un número entero.");
            }
        }
    }

    public static int leerEnteroRango(String mensaje, int min, int max) {
        int numero = leerEntero(mensaje);
        while (numero < min || numero > max) {
            System.out.println("El número tiene que estar entre " + min + " y " + max + ".");
            numero = leerEntero(mensaje);
        }
        return numero;
    }

    // Devuelve la fecha como texto "yyyy-MM-dd" ya comprobada
    public static String leerFecha(String mensaje) {
        while (true) {
            String texto = leerLinea(mensaje + " (aaaa-mm-dd): ");
            try {
                LocalDate.parse(texto);
                return texto;
            } catch (DateTimeParseException e) {
                System.out.println("Fecha no válida. Ejemplo: 2026-10-24");
            }
        }
    }

    // Devuelve la hora como texto "HH:mm" ya comprobada
    public static String leerHora(String mensaje) {
        while (true) {
            String texto = leerLinea(mensaje + " (hh:mm): ");
            try {
                LocalTime.parse(texto);
                return texto;
            } catch (DateTimeParseException e) {
                System.out.println("Hora no válida. Ejemplo: 18:30");
            }
        }
    }

    // Para modificar: si se deja vacío se queda el valor actual
    public static int leerEnteroOpcional(String mensaje, int actual, int min, int max) {
        while (true) {
            String texto = leerLinea(mensaje + " [" + actual + "]: ");
            if (texto.isEmpty()) {
                return actual;
            }
            try {
                int numero = Integer.parseInt(texto);
                if (numero >= min && numero <= max) {
                    return numero;
                }
                System.out.println("El número tiene que estar entre " + min + " y " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Tienes que escribir un número entero.");
            }
        }
    }

    // Para modificar: si se deja vacío se queda la fecha actual
    public static String leerFechaOpcional(String mensaje, String actual) {
        while (true) {
            String texto = leerLinea(mensaje + " [" + actual + "]: ");
            if (texto.isEmpty()) {
                return actual;
            }
            try {
                LocalDate.parse(texto);
                return texto;
            } catch (DateTimeParseException e) {
                System.out.println("Fecha no válida. Ejemplo: 2026-10-24");
            }
        }
    }

    // Para modificar: si se deja vacío se queda la hora actual
    public static String leerHoraOpcional(String mensaje, String actual) {
        while (true) {
            String texto = leerLinea(mensaje + " [" + actual + "]: ");
            if (texto.isEmpty()) {
                return actual;
            }
            try {
                LocalTime.parse(texto);
                return texto;
            } catch (DateTimeParseException e) {
                System.out.println("Hora no válida. Ejemplo: 18:30");
            }
        }
    }

    // Para modificar textos: si se deja vacío se queda el valor actual
    public static String leerTextoOpcional(String mensaje, String actual) {
        String texto = leerLinea(mensaje + " [" + actual + "]: ");
        if (texto.isEmpty()) {
            return actual;
        }
        return texto;
    }

    public static boolean leerSiNo(String mensaje) {
        while (true) {
            String texto = leerLinea(mensaje + " (s/n): ").toLowerCase();
            if (texto.equals("s")) {
                return true;
            }
            if (texto.equals("n")) {
                return false;
            }
            System.out.println("Responde s o n.");
        }
    }
}
