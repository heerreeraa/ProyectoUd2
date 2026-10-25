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

        ResultSet rs = metadatos.getTables(catalogo, null, "%", new String[]{"TABLE"});
        ArrayList<InfoTabla> tablas = new ArrayList<>();
        while (rs.next()) {
            InfoTabla tabla = new InfoTabla();
            tabla.setEsquema(rs.getString("TABLE_CAT"));
            tabla.setNombre(rs.getString("TABLE_NAME"));
            tablas.add(tabla);
        }
        rs.close();

        for (InfoTabla tabla : tablas) {
            tabla.setClavePrimaria(obtenerClavePrimaria(metadatos, catalogo, tabla.getNombre()));
            tabla.setColumnas(obtenerColumnas(metadatos, catalogo, tabla.getNombre()));
        }
        return tablas;
    }

    public static ArrayList<InfoColumna> obtenerColumnas(String tabla) throws SQLException {
        Connection conexion = ConexionBD.getInstance().getConnection();
        return obtenerColumnas(conexion.getMetaData(), conexion.getCatalog(), tabla);
    }

    private static ArrayList<String> obtenerClavePrimaria(DatabaseMetaData metadatos, String catalogo, String tabla) throws SQLException {
        ResultSet rs = metadatos.getPrimaryKeys(catalogo, null, tabla);
        ArrayList<String> clavePrimaria = new ArrayList<>();
        while (rs.next()) {
            clavePrimaria.add(rs.getString("COLUMN_NAME"));
        }
        rs.close();
        return clavePrimaria;
    }

    private static ArrayList<InfoColumna> obtenerColumnas(DatabaseMetaData metadatos, String catalogo, String tabla) throws SQLException {
        ResultSet rs = metadatos.getColumns(catalogo, null, tabla, "%");
        ArrayList<InfoColumna> columnas = new ArrayList<>();
        while (rs.next()) {
            InfoColumna columna = new InfoColumna();
            columna.setNombre(rs.getString("COLUMN_NAME"));
            columna.setTipo(rs.getString("TYPE_NAME"));
            columna.setTamanio(rs.getInt("COLUMN_SIZE"));
            columna.setAdmiteNulos(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
            columna.setAutoincremental(rs.getString("IS_AUTOINCREMENT").equals("YES"));
            columna.setValorPorDefecto(rs.getString("COLUMN_DEF"));
            columnas.add(columna);
        }
        rs.close();
        return columnas;
    }
}
