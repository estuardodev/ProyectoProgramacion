package com.estuardodev.proyectoprogramacion.Clases;

import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;

import java.io.Serializable;
import java.sql.ResultSet;

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

    public ResultSet getIdTelefono(){
        String id_code_query = "SELECT id FROM codigotelefono WHERE codigo = '" + codigo + "'";
        ResultSet rs= DbConexion.ConsultaSQL(id_code_query);
        return  rs;
    }
}
