package com.estuardodev.proyectoprogramacion.Admin;

import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;
import com.estuardodev.proyectoprogramacion.ProyectoApplication;
import com.estuardodev.proyectoprogramacion.StageAwareController;
import com.estuardodev.proyectoprogramacion.Usuario;
import com.estuardodev.proyectoprogramacion.Utilidades.Encrypt;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLogin implements StageAwareController {
    private Stage stage;
    Usuario usuario = new Usuario();
    @FXML
    TextField nombre, dpi, correo, codigo, telefono, direccion, username, password1, password2;
    @FXML
    CheckBox acepto;
    @FXML
    Button Crear;
    @FXML
    Label Informacion;
    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void acepto(){
        if (acepto.isSelected()){
            Crear.setDisable(false);
        }else{
            Crear.setDisable(true);
        }
    }

    @FXML
    protected void Crear(){
        String name = nombre.getText();
        String document = dpi.getText();
        String email = correo.getText();
        String code = codigo.getText();
        String number = telefono.getText();
        String address = direccion.getText();
        String user = username.getText();
        String pass1 = password1.getText();
        String pass2 = password2.getText();
        String pass = Encrypt.getSHA256(pass1);
        String id_code;

        if (name.isBlank() || document.isBlank() || email.isBlank() || code.isBlank()
            || number.isBlank() || address.isBlank() || pass1.isBlank() || pass2.isBlank() || user.isBlank()){
            Informacion.setVisible(true);
            Informacion.setText("Rellena todos los campos correctamente :|");
            Informacion.setTextFill(Color.RED);
        }else{
            try {
                String query = "SELECT id FROM codigotelefono WHERE codigo = '"+code+"'";
                ResultSet rs = DbConexion.ConsultaSQL(query);
                if (rs.next()){
                    id_code = rs.getString("id");
                }else{
                    query = "INSERT INTO codigotelefono (codigo) VALUES ('"+code+"')";
                    DbConexion.ejecutarInsercion(query);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }


            String query_get = "SELECT id FROM codigotelefono WHERE codigo = '"+code+"'";
            ResultSet rs = DbConexion.ConsultaSQL(query_get);
            try {
                if (rs.next()){
                    id_code = rs.getString("id");
                    if(!pass1.equals(pass2)){
                        Informacion.setVisible(true);
                        Informacion.setText("Las contraseñas no coinciden :|");
                        Informacion.setTextFill(Color.YELLOWGREEN);
                    }else {
                        usuario.setDatos("", name, document, id_code, number, user, email, pass, address, true);
                        int info = usuario.CrearUsuario();
                        switch (info){
                            case 0:
                                File file = new File("Biblioteca/init.txt");
                                file.createNewFile();
                                FileWriter fw = new FileWriter(file);
                                BufferedWriter bw = new BufferedWriter(fw);
                                bw.write("true");
                                ProyectoApplication pa = new ProyectoApplication();
                                pa.mostrarVista(stage, "Login-view.fxml");
                                break;
                        }
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
