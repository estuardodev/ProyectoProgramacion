package com.estuardodev.proyectoprogramacion.DataBase;

import com.estuardodev.proyectoprogramacion.Utilidades.Utils;

import java.sql.*;

public class DbConexion {

    static String url = Utils.getStringJson("Biblioteca/secrets.json", "url");;
    static String db = Utils.getStringJson("Biblioteca/secrets.json", "database");;
    static String user = Utils.getStringJson("Biblioteca/secrets.json", "user");;
    static String password = Utils.getStringJson("Biblioteca/secrets.json", "password");;
    static String host = Utils.getStringJson("Biblioteca/secrets.json", "host");;
    static String port = Utils.getStringJson("Biblioteca/secrets.json", "port");;
    static String URLPostgres = url + host + ":" + port + "/" + db;

    public static Connection Conexion() {
        Connection connection = null;
        try {
            System.out.println("Intentando conectar a la base de datos con la URL: " + URLPostgres);
            connection = DriverManager.getConnection(URLPostgres, user, password);
            System.out.println("Conexión establecida correctamente.");
        } catch (Exception e) {
            System.out.println("Hubo un problema al conectar con la base de datos.");
            System.out.println(e);
        }
        return connection;
    }

    public static ResultSet ConsultaSQL(String query){
        ResultSet rs;
        try {
            Connection connection = DbConexion.Conexion();
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }
    public static ResultSet ConsultaSQL(String query, String parametro) {
        ResultSet rs;
        try {
            Connection connection = DbConexion.Conexion();
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, parametro);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }

    public static void ejecutarInsercion(String query) {
        try {
            Connection connection = DbConexion.Conexion();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException("Error al ejecutar la consulta de inserción: " + e.getMessage(), e);
        }
    }

    public static void ejecutarUpdate(String query) {
        try {
            Connection connection = DbConexion.Conexion();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException("Error al ejecutar la consulta de actualización: " + e.getMessage(), e);
        }
    }


    public static void eliminarRegistro(String tabla, String campo, String campo2, String valor1, String valor2) {
        try {
            Connection connection = DbConexion.Conexion();
            String query = "DELETE FROM " + tabla + " WHERE " + campo + " = ? AND "+campo2+"= ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, valor1);
            pstmt.setString(2, valor2);
            pstmt.executeUpdate();
            System.out.println("Registro eliminado correctamente de la tabla " + tabla);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar registro de la tabla " + tabla + ": " + e.getMessage(), e);
        }
    }

}
