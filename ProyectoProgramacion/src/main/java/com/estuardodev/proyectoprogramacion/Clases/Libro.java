package com.estuardodev.proyectoprogramacion.Clases;

import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;

public class Libro implements Serializable{
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
}