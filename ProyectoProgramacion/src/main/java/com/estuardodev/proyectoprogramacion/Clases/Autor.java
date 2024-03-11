package com.estuardodev.proyectoprogramacion.Clases;

import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;

import java.io.Serializable;

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
}