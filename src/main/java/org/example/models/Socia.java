package org.example.models;

public class Socia {
    private String dni;
    private String nombre;
    private String apellidos;
    private String telefono;
    private int edad;
    private String email;
    private boolean eliminada;

    public Socia() {
    }

    public Socia(String dni, String nombre, String apellidos, String telefono, int edad, String email, boolean eliminada) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.edad = edad;
        this.email = email;
        this.eliminada = eliminada;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEliminada() {
        return eliminada;
    }

    public void setEliminada(boolean eliminada) {
        this.eliminada = eliminada;
    }

    @Override
    public String toString() {
        return "[" + dni + "] " + nombre + " " + apellidos + " | " + edad + " años | Tel: " + telefono + " | " + email
                + (eliminada ? " | DADA DE BAJA" : "");
    }
}
