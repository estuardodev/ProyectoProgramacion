package com.estuardodev.proyectoprogramacion;

import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;
import com.estuardodev.proyectoprogramacion.Utilidades.Encrypt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController implements StageAwareController{

    private Stage stage;

    @FXML
    public TextField txtUsername;
    @FXML
    public Button btnLogin, btnCreate;
    @FXML
    public PasswordField txtPass;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void LoginUser() {
        String user = txtUsername.getText();
        String password = Encrypt.getSHA256(txtPass.getText());

        String consulta = "SELECT id, username, password, es_admin, activo FROM usuario WHERE username = ?";
        ResultSet rs = DbConexion.ConsultaSQL(consulta, user);
        boolean activo;

        try {
            if (rs.next()) {
                String dbPassword = rs.getString("password");
                if (dbPassword.equals(password) && (activo = rs.getBoolean("activo"))) {
                    boolean esAdmin = rs.getBoolean("es_admin");


                    if (esAdmin){
                        System.out.println("AUTHENTICADO ADMIN");
                        File file = new File("id.txt");
                        if (!file.exists()){
                            file.createNewFile();
                        }
                        try {
                            FileWriter fw = new FileWriter(file);
                            BufferedWriter bw = new BufferedWriter(fw);
                            bw.write(rs.getString("id"));
                            bw.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        ProyectoApplication pa = new ProyectoApplication();
                        pa.mostrarVista(stage, "DashboardAdmin.fxml");
                    }else {
                        File file = new File("id.txt");
                        if (!file.exists()){
                            file.createNewFile();
                        }
                        try {
                            FileWriter fw = new FileWriter(file);
                            BufferedWriter bw = new BufferedWriter(fw);
                            bw.write(rs.getString("id"));
                            bw.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        ProyectoApplication pa = new ProyectoApplication();
                        pa.mostrarVista(stage, "DashboardUser.fxml");
                    }

                    // Aquí puedes hacer lo que necesites con la información del usuario autenticado
                } else {
                    // La contraseña no coincide
                    System.out.println("Contraseña incorrecta");
                }
            } else {
                // No se encontró ningún usuario con ese nombre de usuario
                System.out.println("Usuario no encontrado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void CreateUser() throws IOException {
        ProyectoApplication pa = new ProyectoApplication();
        pa.mostrarVista(stage, "Create-view.fxml");
    }

}
