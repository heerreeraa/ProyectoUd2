package org.example.menu;

import org.example.dao.AsociacionDao;
import org.example.dao.AsociacionDaoImpl;
import org.example.models.Asociacion;
import org.example.utils.MensajesError;
import org.example.utils.Teclado;
import org.example.utils.Validador;

import java.sql.SQLException;

public class MenuAsociacion {

    private static AsociacionDao asociacionDao = new AsociacionDaoImpl();

    public static void mostrar() {
        int opcion;
        do {
            System.out.println("\n===== DATOS DE LA ASOCIACIÓN =====");
            System.out.println("1. Ver datos");
            System.out.println("2. Modificar datos");
            System.out.println("0. Volver");
            opcion = Teclado.leerEntero("Elige una opción: ");

            try {
                switch (opcion) {
                    case 1:
                        ver();
                        break;
                    case 2:
                        modificar();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (SQLException e) {
                System.out.println(MensajesError.traducir(e));
            }
        } while (opcion != 0);
    }

    private static void ver() throws SQLException {
        Asociacion asociacion = asociacionDao.obtener();
        if (asociacion == null) {
            System.out.println("No hay datos de la asociación.");
        } else {
            System.out.println(asociacion);
        }
    }

    private static void modificar() throws SQLException {
        Asociacion asociacion = asociacionDao.obtener();
        if (asociacion == null) {
            System.out.println("No hay datos de la asociación.");
            return;
        }
        System.out.println("Deja el campo vacío para no cambiarlo.");
        asociacion.setRazonSocial(Teclado.leerTextoOpcional("Razón social", asociacion.getRazonSocial()));

        String cif = Teclado.leerTextoOpcional("CIF", asociacion.getCif()).toUpperCase();
        while (!Validador.esCifValido(cif)) {
            System.out.println("CIF no válido. Ejemplo: G01234567");
            cif = Teclado.leerTextoOpcional("CIF", asociacion.getCif()).toUpperCase();
        }
        asociacion.setCif(cif);

        asociacion.setDireccion(Teclado.leerTextoOpcional("Dirección", asociacion.getDireccion()));

        String telefono = Teclado.leerTextoOpcional("Teléfono", asociacion.getTelefono());
        while (!Validador.esTelefonoValido(telefono)) {
            System.out.println("Teléfono no válido. Escribe entre 9 y 15 cifras.");
            telefono = Teclado.leerTextoOpcional("Teléfono", asociacion.getTelefono());
        }
        asociacion.setTelefono(telefono);

        String email = Teclado.leerTextoOpcional("Email", asociacion.getEmail());
        while (!Validador.esEmailValido(email)) {
            System.out.println("Email no válido. Ejemplo: info@asociacion.eus");
            email = Teclado.leerTextoOpcional("Email", asociacion.getEmail());
        }
        asociacion.setEmail(email);

        asociacion.setFechaApertura(Teclado.leerFechaOpcional("Fecha de apertura", asociacion.getFechaApertura()));
        asociacionDao.modificar(asociacion);
        System.out.println("Datos de la asociación modificados.");
    }
}
