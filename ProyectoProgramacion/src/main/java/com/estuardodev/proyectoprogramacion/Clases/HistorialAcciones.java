package com.estuardodev.proyectoprogramacion.Clases;

import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;

import java.io.Serializable;
import java.util.Date;

public class HistorialAcciones implements Serializable {
    private int id;
    private int usuarioId;
    private String accion;
    private Date fecha;

    public HistorialAcciones() {
    }

    public HistorialAcciones(int id, int usuarioId, String accion, Date fecha) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.accion = accion;
        this.fecha = fecha;
    }

    public void CrearHistorialAcciones(){
        String query = "INSERT INTO historial_acciones (usuario_id, accion, fecha) VALUES ('"+usuarioId+"', '"+accion+"', '"+fecha+"')";
        DbConexion.ejecutarInsercion(query);
    }
}