package com.estuardodev.proyectoprogramacion;

import java.sql.*;

public class DbConexion {
    static String url = "jdbc:postgresql://";
    static String db = "Proyecto";
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

    public static ResultSet ConsultaSQL(String Query){
        ResultSet rs;
        try {
            Connection connection = DbConexion.Conexion();
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(Query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }

}
