package org.example;

import org.example.utils.Validador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidadorTest {

    @Test
    public void dniConLetraCorrecta() {
        assertTrue(Validador.esDocumentoValido("12345678Z"));
    }

    @Test
    public void dniConLetraIncorrecta() {
        assertFalse(Validador.esDocumentoValido("12345678A"));
    }

    @Test
    public void nieValido() {
        assertTrue(Validador.esDocumentoValido("X1234567L"));
    }

    @Test
    public void pasaporteValido() {
        assertTrue(Validador.esDocumentoValido("PAB123456"));
    }

    @Test
    public void documentoDemasiadoCorto() {
        assertFalse(Validador.esDocumentoValido("123"));
    }

    @Test
    public void emailValido() {
        assertTrue(Validador.esEmailValido("ane.etxeberria@mail.com"));
    }

    @Test
    public void emailSinArrobaOSinDominio() {
        assertFalse(Validador.esEmailValido("ane.mail.com"));
        assertFalse(Validador.esEmailValido("ane@mail"));
    }

    @Test
    public void telefonos() {
        assertTrue(Validador.esTelefonoValido("688111222"));
        assertTrue(Validador.esTelefonoValido("+34688111222"));
        assertFalse(Validador.esTelefonoValido("12ab"));
    }

    @Test
    public void cif() {
        assertTrue(Validador.esCifValido("G01234567"));
        assertFalse(Validador.esCifValido("X123"));
    }
}
