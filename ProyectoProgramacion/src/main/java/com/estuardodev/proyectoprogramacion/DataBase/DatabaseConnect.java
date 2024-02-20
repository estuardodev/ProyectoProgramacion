package com.estuardodev.proyectoprogramacion.DataBase;

import com.estuardodev.proyectoprogramacion.ProyectoApplication;
import com.estuardodev.proyectoprogramacion.StageAwareController;
import com.estuardodev.proyectoprogramacion.Utilidades.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DatabaseConnect implements StageAwareController, Initializable {
    Stage stage;
    ProyectoApplication pa = new ProyectoApplication();

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    TextField txtUser, txtPassword, txtHost, txtPort, txtDatabase, txtResend;
    @FXML
    Button btnGuardar, btnContinuar;
    @FXML
    Label lbMensaje;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File file = new File("secrets.json");
        try {
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter br = new BufferedWriter(fw);

            String estructura = "{\n" +
                    "  \"url\" : \"jdbc:postgresql://\",\n" +
                    "  \"database\" : \"\",\n" +
                    "  \"user\" : \"\",\n" +
                    "  \"password\" : \"\",\n" +
                    "  \"host\" : \"\",\n" +
                    "  \"port\" : \"\",\n" +
                    "\n" +
                    "  \"resend\" : \"\"\n" +
                    "}";
            br.write(estructura);

            br.close();
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    protected void btnGuardar(){
        String user = txtUser.getText();
        String pass = txtPassword.getText();
        String host = txtHost.getText();
        String port = txtPort.getText();
        String database = txtDatabase.getText();
        String resend = txtResend.getText();
        if (port.isBlank()){
            port = "5432";
        }
        if (user.isBlank() || pass.isBlank() || host.isBlank() || port.isBlank() || database.isBlank()
        || resend.isBlank()){
            lbMensaje.setVisible(true);
            lbMensaje.setText("Llene los datos correctamente.");
            lbMensaje.setTextFill(Color.RED);
        }else{
            Utils.setStringJson("secrets.json", "database", database);
            Utils.setStringJson("secrets.json", "user", user);
            Utils.setStringJson("secrets.json", "password", pass);
            Utils.setStringJson("secrets.json", "host", host);
            Utils.setStringJson("secrets.json", "port", port);
            Utils.setStringJson("secrets.json", "resend", resend);
            try {
                DbConexion.Conexion();
                btnGuardar.setDisable(true);
                txtUser.setDisable(true);
                txtPassword.setDisable(true);
                txtHost.setDisable(true);
                txtPort.setDisable(true);
                txtDatabase.setDisable(true);
                txtResend.setDisable(true);

                lbMensaje.setVisible(true);
                lbMensaje.setText("Conectado exitosamente a la base de datos.");
                lbMensaje.setTextFill(Color.GREENYELLOW);

                btnContinuar.setDisable(false);
                btnGuardar.setDisable(true);
            }catch (Exception e){
                lbMensaje.setVisible(true);
                lbMensaje.setText("Error al conectar a la base de datos.");
                lbMensaje.setTextFill(Color.RED);
                e.printStackTrace();
            }
        }

    }
    @FXML
    protected void btnContinuar() throws IOException {
        Utils.CreateTables();
        pa.mostrarVista(stage, "FirstLogin.fxml");
    }
}
