package com.estuardodev.proyectoprogramacion.Admin;

import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.io.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class AdminMetodos {

    protected void EliminarCampo(String tabla, String campo, CheckBox cb, ComboBox<String> cx, String Mensaje, Label lb, ResultSet user){
        if (cb.isSelected()){
            try {
                String query = "DELETE FROM "+tabla+" WHERE "+campo+" = '"+cx.getValue()+"'";
                DbConexion.ejecutarUpdate(query);
                cx.getSelectionModel().clearSelection();
                comboBoxConsultar(campo, tabla, cx);
                GuardarAccion("una", user, "eliminación");
                lb.setVisible(true);
                lb.setText(Mensaje+" eliminada exitosamente.");
            }catch (Exception e){
                e.printStackTrace();
                lb.setVisible(true);
                lb.setText("El atributo tiene libros publicados.");
            }
        }else {
            lb.setVisible(true);
            lb.setText("Acepta los términos");
        }
    }

    protected void GuardarElemento(TextField txt, String tabla, String campo, Label lb, ResultSet user){
        String name = txt.getText();
        String query = "SELECT * FROM "+tabla+" WHERE "+campo+" = '" + name + "'";
        ResultSet rs = DbConexion.ConsultaSQL(query);
        try {
            if (!rs.next()){
                String query_insert = "INSERT INTO "+tabla+" ("+campo+") VALUES ('"+name+"')";
                DbConexion.ejecutarInsercion(query_insert);
                GuardarAccion("un", user, "nuevo registro");
                lb.setVisible(true);
            }else {
                lb.setVisible(true);
                lb.setText("El elemento ya existe.");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    protected void ActualizarElemento(String tabla, String campo, TextField txt, ComboBox<String> cx, Label lb, ResultSet user){
        String query = "UPDATE "+tabla+" SET "+campo+" = '"+txt.getText()+"' WHERE "+campo+" ='"+cx.getValue()+"'";
        DbConexion.ejecutarUpdate(query);
        cx.getSelectionModel().clearSelection();
        GuardarAccion("una", user, "actualización");
        lb.setVisible(true);
    }

    public void cargarListView(ListView listView, String columna, String tabla){
        try {
            String consulta = "SELECT " + columna + " FROM " + tabla;
            ResultSet rs = DbConexion.ConsultaSQL(consulta);

            ObservableList<String> items = FXCollections.observableArrayList();
            while (rs.next()) {
                items.add(rs.getString(columna));
            }

            listView.setItems(items);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void cargarListView(ListView listView, String columna, String tabla, String user){
        try {
            String consulta = "SELECT " + columna + " FROM " + tabla + " WHERE usuario_id = '"+user+"'";
            ResultSet rs = DbConexion.ConsultaSQL(consulta);

            ObservableList<String> items = FXCollections.observableArrayList();
            while (rs.next()) {
                items.add(rs.getString(columna));
            }

            listView.setItems(items);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet perfilUser(){
        File file = new File("Biblioteca/id.txt");
        ResultSet rs = null;
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String id = br.readLine();
            rs = DbConexion.ConsultaSQL("SELECT * FROM usuario WHERE id= '"+id+"'");
            return rs;
        }catch (Exception e){
            e.printStackTrace();
        }
        return rs;
    }

    public void comboBoxConsultar(String columna, String tabla, ComboBox<String> cb){
        try {
            String consulta = "SELECT "+columna+" FROM "+tabla;
            ResultSet rs = DbConexion.ConsultaSQL(consulta);

            ObservableList<String> options = FXCollections.observableArrayList();
            while (rs.next()){
                options.add(rs.getString(columna));
            }
            cb.setItems(options);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected String obtenerNombrePorId(String tabla, String campoNombre, String campoId, int id) throws SQLException {
        String query = "SELECT " + campoNombre + " FROM " + tabla + " WHERE " + campoId + " = " + id;
        ResultSet rs = DbConexion.ConsultaSQL(query);
        if (rs.next()) {
            return rs.getString(campoNombre);
        }
        return null;
    }

    protected JsonArray convertResultSetToJson(ResultSet resultSet) throws SQLException {
        JsonArray jsonArray = new JsonArray();
        while (resultSet.next()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("titulo", resultSet.getString("titulo"));
            jsonObject.addProperty("fecha_publicacion", resultSet.getString("fecha_publicacion"));
            jsonObject.addProperty("isbn", resultSet.getString("isbn"));
            jsonObject.addProperty("cantidad_stock", resultSet.getInt("cantidad_stock"));
            // Obtener los nombres de autor y editorial a partir de las FKs
            int fkIdAutor = resultSet.getInt("fkidautor");
            int fkIdEditorial = resultSet.getInt("fkideditorial");
            String nombreAutor = obtenerNombrePorId("autor", "nombre", "id", fkIdAutor);
            String nombreEditorial = obtenerNombrePorId("editorial", "nombre", "id", fkIdEditorial);
            jsonObject.addProperty("autor", nombreAutor);
            jsonObject.addProperty("editorial", nombreEditorial);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
    protected void saveJsonToFile(JsonArray jsonArray, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                writer.write(jsonObject.toString());
                if (i < jsonArray.size() - 1) {
                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exportación de datos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    public void GuardarAccion(String tipo_CRUD, ResultSet rs, String accion){
        try {
            if (rs.next()) {
                String recolectar = rs.getString("recopilar");
                if (recolectar.equals("true") || recolectar.equals("t")){
                    String id = rs.getString("id");
                    String _accion = "El usuario " + rs.getString("username") +
                            " realizó " + tipo_CRUD+" " + accion + " - " + obtenerFechaActual(true, false);
                    String fecha = obtenerFechaActual(true, false);
                    String query = "INSERT INTO historial_acciones (usuario_id, accion, fecha) " +
                            "VALUES ('"+id+"', '"+_accion+"', '"+fecha+"')";

                    DbConexion.ejecutarInsercion(query);
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static String obtenerFechaActual(boolean hora, boolean prestado) {
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter;
        if(hora){
            LocalDateTime fechaHoraActual = LocalDateTime.now();
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fechaHoraFormateada = fechaHoraActual.format(formatter);
            return fechaHoraFormateada;
        }else if(prestado) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate fechaMas14Dias = fechaActual.plusDays(14);
            String fechaMas14DiasFormateada = fechaMas14Dias.format(formatter);

            return fechaMas14DiasFormateada;

        }else{
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String fechaFormateada = fechaActual.format(formatter);

                return fechaFormateada;
            }
        }
    }


