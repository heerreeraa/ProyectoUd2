package org.example.metadatos;

import org.example.utils.ConexionBD;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Metadatos {

    public static InfoBBDD obtenerInfoBBDD() throws SQLException {
        Connection conexion = ConexionBD.getInstance().getConnection();
        DatabaseMetaData metadatos = conexion.getMetaData();

        InfoBBDD info = new InfoBBDD();
        info.setNombreBBDD(conexion.getCatalog());
        info.setNombreSGBD(metadatos.getDatabaseProductName());
        info.setVersionSGBD(metadatos.getDatabaseProductVersion());
        info.setNombreDriver(metadatos.getDriverName());
        info.setVersionDriver(metadatos.getDriverVersion());
        info.setUrl(metadatos.getURL());
        info.setUsuario(metadatos.getUserName());
        return info;
    }

    public static ArrayList<InfoTabla> obtenerTablas() throws SQLException {
        Connection conexion = ConexionBD.getInstance().getConnection();
        DatabaseMetaData metadatos = conexion.getMetaData();
        String catalogo = conexion.getCatalog();
        ResultSet resultado = null;
        ArrayList<InfoTabla> tablas = new ArrayList<>();

        try {
            resultado = metadatos.getTables(catalogo, null, "%", new String[]{"TABLE"});
            while (resultado.next()) {
                InfoTabla tabla = new InfoTabla();
                tabla.setEsquema(resultado.getString("TABLE_SCHEM"));
                tabla.setNombre(resultado.getString("TABLE_NAME"));
                tabla.setClavePrimaria(obtenerClavePrimaria(metadatos, catalogo, tabla.getEsquema(), tabla.getNombre()));
                tabla.setColumnas(obtenerColumnas(metadatos, catalogo, tabla.getEsquema(), tabla.getNombre()));
                tablas.add(tabla);
            }
        } finally {
            if (resultado != null) {
                resultado.close();
            }
        }
        return tablas;
    }

    public static ArrayList<InfoColumna> obtenerColumnas(String tabla) throws SQLException {
        Connection conexion = ConexionBD.getInstance().getConnection();
        return obtenerColumnas(conexion.getMetaData(), conexion.getCatalog(), null, tabla);
    }

    private static ArrayList<String> obtenerClavePrimaria(DatabaseMetaData metadatos, String catalogo,
                                                           String esquema, String tabla) throws SQLException {
        ResultSet resultado = null;
        ArrayList<String> clavePrimaria = new ArrayList<>();

        try {
            resultado = metadatos.getPrimaryKeys(catalogo, esquema, tabla);
            while (resultado.next()) {
                clavePrimaria.add(resultado.getString("COLUMN_NAME"));
            }
        } finally {
            if (resultado != null) {
                resultado.close();
            }
        }
        return clavePrimaria;
    }

    private static ArrayList<InfoColumna> obtenerColumnas(DatabaseMetaData metadatos, String catalogo,
                                                          String esquema, String tabla) throws SQLException {
        ResultSet resultado = null;
        ArrayList<InfoColumna> columnas = new ArrayList<>();

        try {
            resultado = metadatos.getColumns(catalogo, esquema, tabla, "%");
            while (resultado.next()) {
                InfoColumna columna = new InfoColumna();
                columna.setNombre(resultado.getString("COLUMN_NAME"));
                columna.setTipo(resultado.getString("TYPE_NAME"));
                columna.setTamanio(resultado.getInt("COLUMN_SIZE"));
                columna.setAdmiteNulos(resultado.getInt("NULLABLE") != DatabaseMetaData.columnNoNulls);
                columna.setAutoincremental("YES".equalsIgnoreCase(resultado.getString("IS_AUTOINCREMENT")));
                columna.setValorPorDefecto(resultado.getString("COLUMN_DEF"));
                columnas.add(columna);
            }
        } finally {
            if (resultado != null) {
                resultado.close();
            }
        }
        return columnas;
    }
}
