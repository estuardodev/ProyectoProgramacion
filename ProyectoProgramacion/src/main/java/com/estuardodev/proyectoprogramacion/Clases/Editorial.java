package com.estuardodev.proyectoprogramacion.Clases;

import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;

import java.io.Serializable;

public class Editorial implements Serializable {
    private int id;
    private String nombre;

    public Editorial() {
    }

    public Editorial(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public void CrearEditorial(){
        String query = "INSERT INTO editorial (nombre) VALUES ('"+nombre+"')";
        DbConexion.ejecutarInsercion(query);
    }
}