package com.estuardodev.proyectoprogramacion.Clases;

import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public ArrayList<CodigoTelefono> obtenerTodos() throws SQLException {

        ArrayList<CodigoTelefono> telefonos = new ArrayList<>();

        String id_code_query = "SELECT * FROM codigotelefono";
        ResultSet rs= DbConexion.ConsultaSQL(id_code_query);

        while (rs.next()){
            CodigoTelefono telefono = new CodigoTelefono();
            telefono.setId(rs.getInt(1));
            telefono.setCodigo(rs.getInt(2));

            telefonos.add(telefono);
        }

        return telefonos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
