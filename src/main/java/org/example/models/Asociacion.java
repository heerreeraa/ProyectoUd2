package org.example.models;

// Datos de la propia asociación (en la BBDD solo hay una fila)
public class Asociacion {
    private int id;
    private String razonSocial;
    private String cif;
    private String direccion;
    private String telefono;
    private String email;
    private String fechaApertura;

    public Asociacion() {
    }

    public Asociacion(int id, String razonSocial, String cif, String direccion, String telefono, String email, String fechaApertura) {
        this.id = id;
        this.razonSocial = razonSocial;
        this.cif = cif;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.fechaApertura = fechaApertura;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(String fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    @Override
    public String toString() {
        return razonSocial + "\n"
                + "CIF: " + cif + "\n"
                + "Dirección: " + direccion + "\n"
                + "Teléfono: " + telefono + " | Email: " + email + "\n"
                + "Fecha de apertura: " + fechaApertura;
    }
}
