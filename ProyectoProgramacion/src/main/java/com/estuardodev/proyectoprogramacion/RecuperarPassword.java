package com.estuardodev.proyectoprogramacion;

import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;
import com.estuardodev.proyectoprogramacion.Utilidades.Encrypt;
import com.estuardodev.proyectoprogramacion.Utilidades.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecuperarPassword  implements StageAwareController{

    @FXML
    private Label lbCorreo, lbCode, lbNoChange, lbCambiado;
    @FXML
    private TextField txtCorreo, txtCode;
    @FXML
    private PasswordField txtPass1, txtPass2;
    @FXML
    private Button btnGetCode, btnVerify, btnChange, btnBack;
    @FXML
    private Pane paCorreo, paCode, paChanges;

    private Stage stage;
    private String email_global;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void btnGetCode() {
        String email = txtCorreo.getText();
        email_global = email;
        try {
            ResultSet rs = DbConexion.ConsultaSQL("SELECT password, resend FROM usuario WHERE email = '"+email+"'");

            if (rs.next()){
                String code = Utils.GenerarCode();
                DbConexion.ejecutarUpdate("UPDATE usuario SET resend = '"+code+"' WHERE email = '"+email+"'" );
                paCorreo.setVisible(false);
                Utils.SendCode(code, email);
                paCode.setVisible(true);
            }else{
                lbCorreo.setVisible(true);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @FXML
    protected void btnVerify() {
        String codeget = txtCode.getText();
        try {
            ResultSet rs = DbConexion.ConsultaSQL("SELECT resend FROM usuario WHERE email='"+email_global+"'");
            if (rs.next()){
                if (rs.getString("resend").equals(codeget)){
                    DbConexion.ejecutarUpdate("UPDATE usuario SET resend = '' WHERE email = '"+email_global+"'");
                    paCode.setVisible(false);
                    paChanges.setVisible(true);
                }else{
                    lbCode.setVisible(true);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    protected void btnChange() {
        String newPass1 = txtPass1.getText();
        String newPass2 = txtPass2.getText();
        if (newPass2.equals(newPass1) && !newPass1.isBlank()){
            try {
                String newPass = Encrypt.getSHA256(newPass1);
                String query = "UPDATE usuario SET password = '"+newPass+"' WHERE email = '"+email_global+"'";
                DbConexion.ejecutarUpdate(query);
                paChanges.setVisible(false);
                lbCambiado.setVisible(true);
            }catch (Exception e){
                e.printStackTrace();
                lbNoChange.setVisible(true);
                lbNoChange.setText("Error al cambiar contraseña");
            }
        }else{
            lbNoChange.setVisible(true);
        }

    }

    @FXML
    protected void btnBack() throws IOException {
        ProyectoApplication pa = new ProyectoApplication();
        pa.mostrarVista(stage, "Login-view.fxml");
    }

}
