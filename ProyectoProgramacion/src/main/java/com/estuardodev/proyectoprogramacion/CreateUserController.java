package com.estuardodev.proyectoprogramacion;

import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;
import com.estuardodev.proyectoprogramacion.Utilidades.Encrypt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CreateUserController extends Usuario implements Initializable, StageAwareController {
    private Stage stage;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Button btnRegister, btnLogin;
    @FXML
    private ComboBox<String> boxCode = new ComboBox<String>();
    @FXML
    private TextField txtNombre, txtDpi,txtNumber, txtAdress, txtUsername, txtCorreo;
    @FXML
    private Label lbMessage;
    @FXML
    private PasswordField txtPass;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarComboBox();
    }


    public void inicializarComboBox() {
        try {
            String consulta = "SELECT codigo FROM codigotelefono";
            ResultSet rs = DbConexion.ConsultaSQL(consulta);

            ObservableList<String> options = FXCollections.observableArrayList();
            while (rs.next()){
                options.add(rs.getString("codigo"));
            }
            boxCode.setItems(options);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void btnRegister() {
        try {
            String nombre = txtNombre.getText();
            String number = txtNumber.getText();
            String dpi = txtDpi.getText();
            String address = txtAdress.getText();
            String code = boxCode.getValue();
            String user = txtUsername.getText();
            String pass = Encrypt.getSHA256(txtPass.getText());
            String email = txtCorreo.getText();

            setDatos("", nombre, dpi, code, number, user, email, pass,
                    address, false);
            int info = CrearUser();
            switch (info){
                case 0:
                    lbMessage.setVisible(true);
                    lbMessage.setTextFill(Color.GREEN);
                    lbMessage.setText("Usuario agregado correctamente.");
                    break;
                case 1:
                    lbMessage.setVisible(true);
                    break;
                case 2:
                    lbMessage.setVisible(true);
                    lbMessage.setText("El nombre de usuario, DPI o email ya existen.");
                    break;
                case 3:
                    lbMessage.setVisible(true);
                    lbMessage.setTextFill(Color.YELLOW);
                    lbMessage.setText("El código de teléfono seleccionado no es válido.");
                    break;
                case 9:
                    lbMessage.setVisible(true);
                    lbMessage.setTextFill(Color.RED);
                    lbMessage.setText("Ocurrio un error interno");
                    break;
            }
        } catch (Exception e) {
            lbMessage.setVisible(true);
            System.out.println(e);
        }
    }


    @FXML
    protected void btnLogin() throws IOException {

        ProyectoApplication pa = new ProyectoApplication();
        pa.mostrarVista(stage, "Login-view.fxml");

    }

}
