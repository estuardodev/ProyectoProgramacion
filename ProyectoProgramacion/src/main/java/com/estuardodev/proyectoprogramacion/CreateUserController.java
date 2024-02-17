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

public class CreateUserController implements Initializable, StageAwareController {
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

            if (nombre.isBlank() || number.isBlank() || address.isBlank() || code.isBlank()
                    || user.isBlank() || pass.isBlank() || dpi.isBlank() || dpi.length() != 13) {
                lbMessage.setVisible(true);
            } else {
                String verificar_user = "SELECT * FROM usuario WHERE username = '" + user + "'";
                String verificar_dpi = "SELECT * FROM usuario WHERE identificador = '" + dpi + "'";
                String verificar_correo = "SELECT * FROM usuario WHERE identificador = '" + email + "'";

                ResultSet user_none = DbConexion.ConsultaSQL(verificar_user);
                ResultSet dpi_none = DbConexion.ConsultaSQL(verificar_dpi);
                ResultSet email_none = DbConexion.ConsultaSQL(verificar_correo);

                if (user_none.next() || dpi_none.next() || email_none.next()) {
                    lbMessage.setVisible(true);
                    lbMessage.setText("El nombre de usuario, DPI o email ya existen.");
                } else {
                    String id_code_query = "SELECT id FROM codigotelefono WHERE codigo = '" + code + "'";
                    ResultSet rs = DbConexion.ConsultaSQL(id_code_query);

                    if (rs.next()) {
                        String id_code = rs.getString("id");

                        String query = "INSERT INTO usuario (nombre, identificador, telefono, direccion, codigo_telefono, username, password, activo, email) VALUES ('" +
                                nombre + "', '" + dpi + "', '" + number + "', '" + address + "', '" + id_code + "', '" + user + "', '" + pass + "', true, '"+email+"')";
                        DbConexion.ejecutarInsercion(query);

                        lbMessage.setVisible(true);
                        lbMessage.setTextFill(Color.GREEN);
                        lbMessage.setText("Usuario agregado correctamente.");

                    } else {
                        lbMessage.setVisible(true);
                        lbMessage.setTextFill(Color.YELLOW);
                        lbMessage.setText("El código de teléfono seleccionado no es válido.");
                    }
                }
            }
        } catch (SQLException e) {
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
