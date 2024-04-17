package com.estuardodev.proyectoprogramacion.Clases;

import com.estuardodev.proyectoprogramacion.Admin.AdminMetodos;
import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;
import com.estuardodev.proyectoprogramacion.Usuario;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Autor implements Serializable {
    private int id;
    private String nombre;

    public Autor() {
    }

    public Autor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public void CrearAutor(){
        String query = "INSERT INTO autor (nombre) VALUES ('"+nombre+"')";
        DbConexion.ejecutarInsercion(query);
    }

    public void Actualizar(ResultSet user){
        PreparedStatement st = null;

        try {
            String sql = "UPDATE autor SET nombre = ? WHERE id = ?";
            st = DbConexion.Conexion().prepareStatement(sql);
            st.setString(1, getNombre());
            st.setInt(2, getId());

            st.executeUpdate();
            AdminMetodos am = new AdminMetodos();
            am.GuardarAccion("una", user, "actualización");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Autor obtenerPorNombre(String nombre) {

        PreparedStatement st = null;
        ResultSet rs = null;
        Autor a = new Autor();

        try {
            String sql = "SELECT * FROM autor WHERE nombre = ?";
            st = DbConexion.Conexion().prepareStatement(sql);
            st.setString(1, nombre);
            rs = st.executeQuery();

            while (rs.next()) {
                a.setId(rs.getInt(1));
                a.setNombre(rs.getString(2));

                return a;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}