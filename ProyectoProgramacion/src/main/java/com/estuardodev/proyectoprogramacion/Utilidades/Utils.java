package com.estuardodev.proyectoprogramacion.Utilidades;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import com.estuardodev.proyectoprogramacion.Admin.AdminMetodos;
import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.resend.*;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

public class Utils {

    AdminMetodos adminMetodos = new AdminMetodos();

    public static String GenerarCode() {
        String code = "";
        String numbers = "0123456789";
        Random random = new Random();

        while (code.length() < 4) {
            int index = random.nextInt(numbers.length());
            code += numbers.charAt(index);
        }

        return code;
    }

    public static void SendCode(String code, String correo){

        String apiKey = "";
        try {
            apiKey = Utils.getStringJson("Biblioteca/secrets.json", "resend");
        }catch (Exception e){
            e.printStackTrace();
        }

        Resend resend = new Resend(apiKey);

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from("Biblioteca <no-reply@mailer.estuardo.dev>")
                .to(correo)
                .subject("Código de restablecimiento de contraseña")
                .html("<h1>Sistema bibliotecario</h1> <br> <p>Este es tu código de restablecimiento de contraseña: <strong>"+code+"</strong></p>")
                .build();
        try {
            SendEmailResponse data = resend.emails().send(sendEmailRequest);
            System.out.println(data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }

    public static int obtenerIdAutor(String nombreAutor) throws SQLException {
        String queryAutor = "SELECT id FROM autor WHERE nombre = ?";
        try (PreparedStatement psAutor = DbConexion.Conexion().prepareStatement(queryAutor)) {
            psAutor.setString(1, nombreAutor);
            ResultSet rsAutor = psAutor.executeQuery();
            if (rsAutor.next()) {
                return rsAutor.getInt("id");
            } else {
                return 0;
            }
        }
    }

    public static int obtenerIdEditorial(String nombreEditorial) throws SQLException {
        String queryEditorial = "SELECT id FROM editorial WHERE nombre = ?";
        try (PreparedStatement psEditorial = DbConexion.Conexion().prepareStatement(queryEditorial)) {
            psEditorial.setString(1, nombreEditorial);
            ResultSet rsEditorial = psEditorial.executeQuery();
            if (rsEditorial.next()) {
                return rsEditorial.getInt("id");
            } else {
                return 0;
            }
        }
    }

    public static String getStringJson(String archivo, String key){
        JsonParser jsonParser = new JsonParser();
        try {
            FileReader fr = new FileReader(archivo);
            JsonObject jsonObject = jsonParser.parse(fr).getAsJsonObject();
            fr.close();
            return jsonObject.get(key).getAsString();
        }catch (Exception e){
            return "None";
        }
    }

    public static void setStringJson(String archivo, String key, String value) {
        JsonParser jsonParser = new JsonParser();
        try {
            FileReader fr = new FileReader(archivo);
            JsonObject jsonObject = jsonParser.parse(fr).getAsJsonObject();
            fr.close();

            jsonObject.addProperty(key, value);

            FileWriter fw = new FileWriter(archivo);
            fw.write(jsonObject.toString());
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ExportarHistorial(ListView<String> list){
        ObservableList<String> items = list.getItems();
        int contador = 1;
        JsonArray jsonArray = new JsonArray();
        for (String item : items) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("item" + contador, item);
            jsonArray.add(jsonObject);
            contador += 1;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo JSON");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo JSON", "*.json"));
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            // Guardar el JSONArray en el archivo JSON seleccionado por el usuario
            try (FileWriter fileWriter = new FileWriter(selectedFile)) {
                fileWriter.write(jsonArray.toString());
                adminMetodos.mostrarMensaje("Datos exportados correctamente a " + selectedFile.getAbsolutePath());
                adminMetodos.GuardarAccion("una", adminMetodos.perfilUser(), "exportación");
            } catch (IOException e) {
                e.printStackTrace();
                adminMetodos.mostrarMensaje("Error al exportar los datos.");
            }
        } else {
            adminMetodos.mostrarMensaje("Exportación cancelada por el usuario.");
        }

    }

    public static void CreateTables(){
        String sql = "-- Create tables if they don't exist\n" +
                "CREATE TABLE IF NOT EXISTS autor (\n" +
                "    id serial PRIMARY KEY,\n" +
                "    nombre varchar(75)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS codigotelefono (\n" +
                "    id serial PRIMARY KEY,\n" +
                "    codigo integer\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS editorial (\n" +
                "    id serial PRIMARY KEY,\n" +
                "    nombre varchar(75)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS usuario (\n" +
                "    id serial PRIMARY KEY,\n" +
                "    identificador varchar(13),\n" +
                "    nombre varchar(75),\n" +
                "    telefono varchar(20),\n" +
                "    cantidad_prestamo integer,\n" +
                "    vencimiento_prestamo date,\n" +
                "    direccion varchar(50),\n" +
                "    multas_pendientes integer,\n" +
                "    total_deudas_pendientes numeric,\n" +
                "    codigo_telefono integer,\n" +
                "    username varchar(20),\n" +
                "    password varchar(100),\n" +
                "    es_admin boolean,\n" +
                "    activo boolean,\n" +
                "    email varchar(100) NOT NULL,\n" +
                "    resend varchar(4),\n" +
                "    recopilar boolean DEFAULT true,\n" +
                "    librosprestados varchar(5),\n" +
                "    ultimopago date,\n" +
                "    CONSTRAINT fk_codigotelefono FOREIGN KEY (codigo_telefono) REFERENCES codigotelefono(id)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS historial_acciones (\n" +
                "    id serial PRIMARY KEY,\n" +
                "    usuario_id integer,\n" +
                "    accion varchar(255),\n" +
                "    fecha timestamp without time zone DEFAULT CURRENT_TIMESTAMP,\n" +
                "    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS libro (\n" +
                "    id serial PRIMARY KEY,\n" +
                "    titulo varchar(75),\n" +
                "    fecha_publicacion date,\n" +
                "    isbn varchar(75),\n" +
                "    cantidad_stock integer,\n" +
                "    fkidautor integer,\n" +
                "    fkideditorial integer,\n" +
                "    CONSTRAINT fk_autor FOREIGN KEY (fkidautor) REFERENCES public.autor(id),\n" +
                "    CONSTRAINT fk_editorial FOREIGN KEY (fkideditorial) REFERENCES editorial(id)\n" +
                ");";

        DbConexion.ejecutarInsercion(sql);
    }
}
