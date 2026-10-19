package org.example.utils;

// Comprobaciones de formato de los datos que se teclean
public class Validador {

    private static final String LETRAS_DNI = "TRWAGMYFPDXBNJZSQVHLCKE";

    // Acepta DNI (12345678Z), NIE (X1234567L) o pasaporte (6 a 15 letras o números)
    public static boolean esDocumentoValido(String documento) {
        if (documento.matches("[0-9]{8}[A-Z]")) {
            return letraCorrecta(documento.substring(0, 8), documento.charAt(8));
        }
        if (documento.matches("[XYZ][0-9]{7}[A-Z]")) {
            // En el NIE la X vale 0, la Y vale 1 y la Z vale 2
            String numero = "XYZ".indexOf(documento.charAt(0)) + documento.substring(1, 8);
            return letraCorrecta(numero, documento.charAt(8));
        }
        return documento.matches("[A-Z0-9]{6,15}");
    }

    private static boolean letraCorrecta(String numero, char letra) {
        int resto = Integer.parseInt(numero) % 23;
        return LETRAS_DNI.charAt(resto) == letra;
    }

    // CIF: una letra de tipo de entidad, 7 cifras y un carácter de control (G01234567)
    public static boolean esCifValido(String cif) {
        return cif.matches("[ABCDEFGHJNPQRSUVW][0-9]{7}[0-9A-J]");
    }

    public static boolean esEmailValido(String email) {
        return email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");
    }

    // Entre 9 y 15 cifras, con un + delante opcional (+34...)
    public static boolean esTelefonoValido(String telefono) {
        return telefono.matches("\\+?[0-9]{9,15}");
    }
}
