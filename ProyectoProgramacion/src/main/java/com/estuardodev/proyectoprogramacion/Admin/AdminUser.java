package com.estuardodev.proyectoprogramacion.Admin;

import com.estuardodev.proyectoprogramacion.ProyectoApplication;
import com.estuardodev.proyectoprogramacion.StageAwareController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminUser implements StageAwareController {

    Stage stage;
    ProyectoApplication pa = new ProyectoApplication();

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void IniciarAdministrador() throws IOException {
        pa.mostrarVista(stage, "DashboardAdmin.fxml");
    }

    @FXML
    protected void IniciarUsuario() throws IOException {
        pa.mostrarVista(stage, "DashboardUser.fxml");
    }

}
