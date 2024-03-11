package com.estuardodev.proyectoprogramacion.Clases;

import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;

import java.io.Serializable;

public class CodigoTelefono implements Serializable {
    private int id;
    private int codigo;

    public CodigoTelefono() {
    }

    public CodigoTelefono(int id, int codigo) {
        this.id = id;
        this.codigo = codigo;
    }

    public void CrearCodigoTelefono(){
        String query = "INSERT INTO codigotelefono (codigo) VALUES ('"+codigo+"')";
        DbConexion.ejecutarInsercion(query);
    }
}
