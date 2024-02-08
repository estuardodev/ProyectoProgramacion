package com.estuardodev.proyectoprogramacion;

import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private TextField search;

    @FXML
    protected void onHelloButtonClick() {
        String consulta = "SELECT * FROM usuarios WHERE nombre = ?";
        try {
            ResultSet rs = DbConexion.ConsultaSQL(consulta, search.getText());
            if (rs.next()) {
                welcomeText.setText("Welcome " + rs.getString("nombre") + " to JavaFX Application!");
            } else {
                welcomeText.setText("User not found");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}