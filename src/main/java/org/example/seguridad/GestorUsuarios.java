package org.example.seguridad;

import org.example.utils.Teclado;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;

public class GestorUsuarios {
    public static final String FICHERO_USUARIOS = "datos/usuarios.dat";
    public static final String FICHERO_SESIONES = "datos/sesiones.log";

    private static String usuarioActual;

    public static boolean login() {
        File fichero = new File(FICHERO_USUARIOS);
        if (!fichero.exists()) {
            System.out.println("Es la primera vez que se ejecuta. Crea el usuario administrador.");
            crearUsuario();
            if (!fichero.exists()) {
                return false;
            }
        }

        ArrayList<Usuario> usuarios = leerUsuarios();
        if (usuarios == null) {
            return false;
        }
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados. No se puede iniciar sesión.");
            return false;
        }

        for (int intento = 1; intento <= 3; intento++) {
            String nombre = Teclado.leerTexto("Usuario: ");
            String password = Teclado.leerTexto("Contraseña: ");
            Usuario usuario = buscarUsuario(usuarios, nombre);
            boolean accesoCorrecto = false;

            if (usuario != null) {
                try {
                    String hash = calcularHash(password, usuario.getSal());
                    accesoCorrecto = hash.equals(usuario.getHashPassword());
                } catch (NoSuchAlgorithmException e) {
                    System.out.println("No se pudo comprobar la contraseña porque SHA-256 no está disponible.");
                    return false;
                } catch (IllegalArgumentException e) {
                    System.out.println("Los datos de la contraseña del usuario están dañados.");
                    return false;
                }
            }

            if (accesoCorrecto) {
                usuarioActual = nombre;
                registrarSesion(usuarioActual, "ENTRADA");
                System.out.println("Inicio de sesión correcto.");
                return true;
            }

            registrarSesion(nombre, "FALLO");
            System.out.println("Usuario o contraseña incorrectos. Intento " + intento + " de 3.");
        }

        System.out.println("Se han agotado los 3 intentos. No se ha iniciado sesión.");
        return false;
    }

    public static String getUsuarioActual() {
        return usuarioActual;
    }

    public static void registrarSesion(String usuario, String accion) {
        File fichero = new File(FICHERO_SESIONES);
        File carpeta = fichero.getParentFile();

        try {
            if (!carpeta.exists() && !carpeta.mkdirs()) {
                System.out.println("No se pudo crear la carpeta datos para guardar las sesiones.");
                return;
            }

            BufferedWriter escritor = new BufferedWriter(new FileWriter(fichero, StandardCharsets.UTF_8, true));
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            escritor.write(fecha + " | " + usuario + " | " + accion);
            escritor.newLine();
            escritor.close();
        } catch (IOException e) {
            System.out.println("No se pudo registrar la sesión: " + e.getMessage());
        }
    }

    public static void crearUsuario() {
        ArrayList<Usuario> usuarios = leerUsuarios();
        if (usuarios == null) {
            return;
        }

        String nombre = Teclado.leerTexto("Nombre de usuario: ");
        if (buscarUsuario(usuarios, nombre) != null) {
            System.out.println("Ya existe un usuario con ese nombre.");
            return;
        }

        String password = Teclado.leerTexto("Contraseña: ");
        String confirmacion = Teclado.leerTexto("Repite la contraseña: ");
        if (!password.equals(confirmacion)) {
            System.out.println("Las contraseñas no coinciden.");
            return;
        }

        try {
            String sal = generarSal();
            String hash = calcularHash(password, sal);
            usuarios.add(new Usuario(nombre, hash, sal));
            if (guardarUsuarios(usuarios)) {
                System.out.println("Usuario creado correctamente.");
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No se pudo crear el usuario porque SHA-256 no está disponible.");
        }
    }

    public static void cambiarPassword() {
        if (usuarioActual == null) {
            System.out.println("No hay ningún usuario con la sesión iniciada.");
            return;
        }

        ArrayList<Usuario> usuarios = leerUsuarios();
        if (usuarios == null) {
            return;
        }
        Usuario usuario = buscarUsuario(usuarios, usuarioActual);
        if (usuario == null) {
            System.out.println("No se encontró el usuario actual.");
            return;
        }

        String passwordActual = Teclado.leerTexto("Contraseña actual: ");
        try {
            if (!calcularHash(passwordActual, usuario.getSal()).equals(usuario.getHashPassword())) {
                System.out.println("La contraseña actual es incorrecta.");
                return;
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No se pudo comprobar la contraseña porque SHA-256 no está disponible.");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("Los datos de la contraseña del usuario están dañados.");
            return;
        }

        String passwordNueva = Teclado.leerTexto("Nueva contraseña: ");
        String confirmacion = Teclado.leerTexto("Repite la nueva contraseña: ");
        if (!passwordNueva.equals(confirmacion)) {
            System.out.println("Las contraseñas no coinciden.");
            return;
        }

        try {
            String salNueva = generarSal();
            usuario.setSal(salNueva);
            usuario.setHashPassword(calcularHash(passwordNueva, salNueva));
            if (guardarUsuarios(usuarios)) {
                System.out.println("Contraseña cambiada correctamente.");
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No se pudo cambiar la contraseña porque SHA-256 no está disponible.");
        }
    }

    public static void verSesiones() {
        File fichero = new File(FICHERO_SESIONES);
        if (!fichero.exists()) {
            System.out.println("Todavía no hay registro de sesiones.");
            return;
        }

        try {
            BufferedReader lector = new BufferedReader(new FileReader(fichero, StandardCharsets.UTF_8));
            String linea = lector.readLine();
            if (linea == null) {
                System.out.println("El registro de sesiones está vacío.");
            }
            while (linea != null) {
                System.out.println(linea);
                linea = lector.readLine();
            }
            lector.close();
        } catch (IOException e) {
            System.out.println("No se pudo leer el registro de sesiones: " + e.getMessage());
        }
    }

    private static ArrayList<Usuario> leerUsuarios() {
        File fichero = new File(FICHERO_USUARIOS);
        if (!fichero.exists()) {
            return new ArrayList<>();
        }

        try {
            ObjectInputStream lector = new ObjectInputStream(new FileInputStream(fichero));
            Object objeto = lector.readObject();
            lector.close();

            if (!(objeto instanceof ArrayList<?>)) {
                System.out.println("El fichero de usuarios está dañado: el contenido no tiene el formato esperado.");
                return null;
            }

            ArrayList<?> listaLeida = (ArrayList<?>) objeto;
            ArrayList<Usuario> usuarios = new ArrayList<>();
            for (Object elemento : listaLeida) {
                if (!(elemento instanceof Usuario)) {
                    System.out.println("El fichero de usuarios está dañado: contiene datos no válidos.");
                    return null;
                }
                Usuario usuario = (Usuario) elemento;
                if (!usuarioValido(usuario)) {
                    System.out.println("El fichero de usuarios está dañado: contiene datos no válidos.");
                    return null;
                }
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (IOException e) {
            System.out.println("No se pudo leer el fichero de usuarios; puede estar dañado: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("No se pudo leer el fichero de usuarios porque contiene una clase desconocida.");
            return null;
        }
    }

    private static boolean guardarUsuarios(ArrayList<Usuario> usuarios) {
        File fichero = new File(FICHERO_USUARIOS);
        File carpeta = fichero.getParentFile();

        try {
            if (!carpeta.exists() && !carpeta.mkdirs()) {
                System.out.println("No se pudo crear la carpeta datos para guardar los usuarios.");
                return false;
            }

            ObjectOutputStream escritor = new ObjectOutputStream(new java.io.FileOutputStream(fichero));
            escritor.writeObject(usuarios);
            escritor.close();
            return true;
        } catch (IOException e) {
            System.out.println("No se pudo guardar el fichero de usuarios: " + e.getMessage());
            return false;
        }
    }

    private static String calcularHash(String password, String sal) throws NoSuchAlgorithmException {
        byte[] bytesSal = Base64.getDecoder().decode(sal);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(bytesSal);
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    private static String generarSal() {
        byte[] bytesSal = new byte[16];
        new SecureRandom().nextBytes(bytesSal);
        return Base64.getEncoder().encodeToString(bytesSal);
    }

    private static Usuario buscarUsuario(ArrayList<Usuario> usuarios, String nombre) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().equals(nombre)) {
                return usuario;
            }
        }
        return null;
    }

    private static boolean usuarioValido(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()
                || usuario.getHashPassword() == null || usuario.getSal() == null) {
            return false;
        }

        try {
            byte[] hash = Base64.getDecoder().decode(usuario.getHashPassword());
            byte[] sal = Base64.getDecoder().decode(usuario.getSal());
            return hash.length == 32 && sal.length == 16;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
