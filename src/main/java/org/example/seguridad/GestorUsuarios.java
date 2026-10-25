package org.example.seguridad;

import org.example.utils.Teclado;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
    private static final int MAX_INTENTOS = 3;

    private static String usuarioActual;

    public static boolean login() {
        if (!new File(FICHERO_USUARIOS).exists()) {
            System.out.println("Es la primera vez que se ejecuta. Crea el usuario administrador.");
            while (!crearUsuario()) {
                System.out.println("Vuelve a intentarlo.");
            }
        }

        ArrayList<Usuario> usuarios = leerUsuarios();
        if (usuarios == null) {
            return false;
        }

        for (int intento = 1; intento <= MAX_INTENTOS; intento++) {
            String nombre = Teclado.leerTexto("Usuario: ");
            String password = Teclado.leerTexto("Contraseña: ");
            Usuario usuario = buscarUsuario(usuarios, nombre);

            if (usuario != null && passwordCorrecta(usuario, password)) {
                usuarioActual = nombre;
                registrarSesion(nombre, "ENTRADA");
                System.out.println("Bienvenida, " + nombre + ".");
                return true;
            }
            registrarSesion(nombre, "FALLO");
            System.out.println("Usuario o contraseña incorrectos. Intento " + intento + " de " + MAX_INTENTOS + ".");
        }
        System.out.println("Se han agotado los intentos.");
        return false;
    }

    public static String getUsuarioActual() {
        return usuarioActual;
    }

    public static void registrarSesion(String usuario, String accion) {
        try {
            crearCarpetaDatos();
            BufferedWriter escritor = new BufferedWriter(new FileWriter(FICHERO_SESIONES, StandardCharsets.UTF_8, true));
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            escritor.write(fecha + " | " + usuario + " | " + accion);
            escritor.newLine();
            escritor.close();
        } catch (IOException e) {
            System.out.println("No se ha podido apuntar la sesión en el registro: " + e.getMessage());
        }
    }

    public static boolean crearUsuario() {
        ArrayList<Usuario> usuarios = leerUsuarios();
        if (usuarios == null) {
            return false;
        }
        String nombre = Teclado.leerTexto("Nombre de usuario: ");
        if (buscarUsuario(usuarios, nombre) != null) {
            System.out.println("Ya existe un usuario con ese nombre.");
            return false;
        }
        String password = pedirPasswordNueva();
        if (password == null) {
            return false;
        }

        String sal = generarSal();
        usuarios.add(new Usuario(nombre, calcularHash(password, sal), sal));
        if (guardarUsuarios(usuarios)) {
            System.out.println("Usuario creado.");
            return true;
        }
        return false;
    }

    public static void cambiarPassword() {
        ArrayList<Usuario> usuarios = leerUsuarios();
        if (usuarios == null) {
            return;
        }
        Usuario usuario = buscarUsuario(usuarios, usuarioActual);
        if (usuario == null) {
            System.out.println("No hay ningún usuario con la sesión iniciada.");
            return;
        }
        if (!passwordCorrecta(usuario, Teclado.leerTexto("Contraseña actual: "))) {
            System.out.println("La contraseña actual no es correcta.");
            return;
        }
        String password = pedirPasswordNueva();
        if (password == null) {
            return;
        }

        String sal = generarSal();
        usuario.setSal(sal);
        usuario.setHashPassword(calcularHash(password, sal));
        if (guardarUsuarios(usuarios)) {
            System.out.println("Contraseña cambiada.");
        }
    }

    public static void verSesiones() {
        File fichero = new File(FICHERO_SESIONES);
        if (!fichero.exists()) {
            System.out.println("Todavía no hay ninguna sesión registrada.");
            return;
        }
        try {
            BufferedReader lector = new BufferedReader(new FileReader(fichero, StandardCharsets.UTF_8));
            String linea = lector.readLine();
            while (linea != null) {
                System.out.println(linea);
                linea = lector.readLine();
            }
            lector.close();
        } catch (IOException e) {
            System.out.println("No se ha podido leer el registro de sesiones: " + e.getMessage());
        }
    }

    private static String pedirPasswordNueva() {
        String password = Teclado.leerTexto("Contraseña: ");
        String repetida = Teclado.leerTexto("Repite la contraseña: ");
        if (!password.equals(repetida)) {
            System.out.println("Las contraseñas no coinciden.");
            return null;
        }
        return password;
    }

    private static boolean passwordCorrecta(Usuario usuario, String password) {
        return calcularHash(password, usuario.getSal()).equals(usuario.getHashPassword());
    }

    @SuppressWarnings("unchecked")
    private static ArrayList<Usuario> leerUsuarios() {
        File fichero = new File(FICHERO_USUARIOS);
        if (!fichero.exists()) {
            return new ArrayList<>();
        }
        try {
            ObjectInputStream lector = new ObjectInputStream(new FileInputStream(fichero));
            ArrayList<Usuario> usuarios = (ArrayList<Usuario>) lector.readObject();
            lector.close();
            return usuarios;
        } catch (IOException e) {
            System.out.println("El fichero " + FICHERO_USUARIOS + " está dañado. Bórralo para crear de nuevo el usuario administrador.");
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("El fichero " + FICHERO_USUARIOS + " no contiene usuarios. Bórralo para crear de nuevo el usuario administrador.");
            return null;
        }
    }

    private static boolean guardarUsuarios(ArrayList<Usuario> usuarios) {
        try {
            crearCarpetaDatos();
            ObjectOutputStream escritor = new ObjectOutputStream(new FileOutputStream(FICHERO_USUARIOS));
            escritor.writeObject(usuarios);
            escritor.close();
            return true;
        } catch (IOException e) {
            System.out.println("No se han podido guardar los usuarios: " + e.getMessage());
            return false;
        }
    }

    private static void crearCarpetaDatos() {
        File carpeta = new File("datos");
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
    }

    private static String calcularHash(String password, String sal) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(Base64.getDecoder().decode(sal));
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No está disponible el algoritmo SHA-256.");
            return "";
        }
    }

    private static String generarSal() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static Usuario buscarUsuario(ArrayList<Usuario> usuarios, String nombre) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().equals(nombre)) {
                return usuario;
            }
        }
        return null;
    }
}
