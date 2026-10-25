package org.example.metadatos;

import java.util.ArrayList;

public class MetadatosCompletos {
    private InfoBBDD bbdd;
    private ArrayList<InfoTabla> tablas = new ArrayList<>();

    public MetadatosCompletos() {
    }

    public MetadatosCompletos(InfoBBDD bbdd, ArrayList<InfoTabla> tablas) {
        this.bbdd = bbdd;
        this.tablas = tablas;
    }

    public InfoBBDD getBbdd() {
        return bbdd;
    }

    public void setBbdd(InfoBBDD bbdd) {
        this.bbdd = bbdd;
    }

    public ArrayList<InfoTabla> getTablas() {
        return tablas;
    }

    public void setTablas(ArrayList<InfoTabla> tablas) {
        this.tablas = tablas;
    }
}
