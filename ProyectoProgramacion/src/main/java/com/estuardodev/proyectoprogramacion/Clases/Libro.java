package com.estuardodev.proyectoprogramacion.Clases;

import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class Libro implements Serializable {
    private int id;
    private String titulo;
    private Date fechaPublicacion;
    private String isbn;
    private int cantidadStock;
    private int autorId;
    private int editorialId;

    public Libro() {
    }

    public Libro(int id, String titulo, Date fechaPublicacion, String isbn, int cantidadStock, int autorId, int editorialId) {
        this.id = id;
        this.titulo = titulo;
        this.fechaPublicacion = fechaPublicacion;
        this.isbn = isbn;
        this.cantidadStock = cantidadStock;
        this.autorId = autorId;
        this.editorialId = editorialId;
    }

    public void CrearLibro() {
        String query = "INSERT INTO libro (titulo, fecha_publicacion, isbn, cantidad_stock, fkidautor, fkideditorial) VALUES ('"
                + titulo + "', '" + fechaPublicacion + "', '" + isbn + "', " + cantidadStock + ", " + autorId + ", " + editorialId + ")";

        DbConexion.ejecutarInsercion(query);
    }

    public ArrayList<Libro> obtenerTodos() {
        ArrayList<Libro> libros = new ArrayList<>();
        String query = "SELECT id, titulo, fecha_publicacion, isbn, cantidad_stock, fkidautor, fkideditorial FROM libro";

        try (Connection conn = DbConexion.Conexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                Date fechaPublicacion = rs.getDate("fecha_publicacion");
                String isbn = rs.getString("isbn");
                int cantidadStock = rs.getInt("cantidad_stock");
                int autorId = rs.getInt("fkidautor");
                int editorialId = rs.getInt("fkideditorial");

                Libro libro = new Libro(id, titulo, fechaPublicacion, isbn, cantidadStock, autorId, editorialId);
                libros.add(libro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(int cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    public int getAutorId() {
        return autorId;
    }

    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    public int getEditorialId() {
        return editorialId;
    }

    public void setEditorialId(int editorialId) {
        this.editorialId = editorialId;
    }
}
