package org.example.metadatos;

public class InfoColumna {
    private String nombre;
    private String tipo;
    private int tamanio;
    private boolean admiteNulos;
    private boolean autoincremental;
    private String valorPorDefecto;

    public InfoColumna() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

    public boolean isAdmiteNulos() {
        return admiteNulos;
    }

    public void setAdmiteNulos(boolean admiteNulos) {
        this.admiteNulos = admiteNulos;
    }

    public boolean isAutoincremental() {
        return autoincremental;
    }

    public void setAutoincremental(boolean autoincremental) {
        this.autoincremental = autoincremental;
    }

    public String getValorPorDefecto() {
        return valorPorDefecto;
    }

    public void setValorPorDefecto(String valorPorDefecto) {
        this.valorPorDefecto = valorPorDefecto;
    }

    @Override
    public String toString() {
        return nombre + " | Tipo: " + tipo + " | Tamaño: " + tamanio
                + " | Admite nulos: " + admiteNulos + " | Autoincremental: " + autoincremental
                + " | Valor por defecto: " + valorPorDefecto;
    }
}
