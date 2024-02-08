package com.estuardodev.proyectoprogramacion.DataBase;

import java.sql.*;

public class DbConexion {
    static String url = "jdbc:postgresql://";
    static String db = "Universidad";
    static String user = "postgres";
    static String password = "123";
    static String host = "localhost";
    static String port = "5432";
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

}
