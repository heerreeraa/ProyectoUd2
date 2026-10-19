package org.example.metadatos;

public class InfoBBDD {
    private String nombreBBDD;
    private String nombreSGBD;
    private String versionSGBD;
    private String nombreDriver;
    private String versionDriver;
    private String url;
    private String usuario;

    public InfoBBDD() {
    }

    public String getNombreBBDD() {
        return nombreBBDD;
    }

    public void setNombreBBDD(String nombreBBDD) {
        this.nombreBBDD = nombreBBDD;
    }

    public String getNombreSGBD() {
        return nombreSGBD;
    }

    public void setNombreSGBD(String nombreSGBD) {
        this.nombreSGBD = nombreSGBD;
    }

    public String getVersionSGBD() {
        return versionSGBD;
    }

    public void setVersionSGBD(String versionSGBD) {
        this.versionSGBD = versionSGBD;
    }

    public String getNombreDriver() {
        return nombreDriver;
    }

    public void setNombreDriver(String nombreDriver) {
        this.nombreDriver = nombreDriver;
    }

    public String getVersionDriver() {
        return versionDriver;
    }

    public void setVersionDriver(String versionDriver) {
        this.versionDriver = versionDriver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Base de datos: " + nombreBBDD + "\n"
                + "SGBD: " + nombreSGBD + " " + versionSGBD + "\n"
                + "Driver: " + nombreDriver + " " + versionDriver + "\n"
                + "URL: " + url + "\n"
                + "Usuario: " + usuario;
    }
}
