package org.example.utils;

public class Validador {

    private static final String LETRAS_DNI = "TRWAGMYFPDXBNJZSQVHLCKE";

    public static boolean esDocumentoValido(String documento) {
        if (documento.matches("[0-9]{8}[A-Z]")) {
            return letraCorrecta(documento.substring(0, 8), documento.charAt(8));
        }
        if (documento.matches("[XYZ][0-9]{7}[A-Z]")) {
            String numero = "XYZ".indexOf(documento.charAt(0)) + documento.substring(1, 8);
            return letraCorrecta(numero, documento.charAt(8));
        }
        return documento.matches("[A-Z0-9]{6,15}");
    }

    private static boolean letraCorrecta(String numero, char letra) {
        int resto = Integer.parseInt(numero) % 23;
        return LETRAS_DNI.charAt(resto) == letra;
    }

    public static boolean esCifValido(String cif) {
        return cif.matches("[ABCDEFGHJNPQRSUVW][0-9]{7}[0-9A-J]");
    }

    public static boolean esEmailValido(String email) {
        return email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");
    }

    public static boolean esTelefonoValido(String telefono) {
        return telefono.matches("\\+?[0-9]{9,15}");
    }
}
