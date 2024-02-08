package com.estuardodev.proyectoprogramacion.Usuarios;

import com.estuardodev.proyectoprogramacion.StageAwareController;
import javafx.stage.Stage;

public class DashboardUser implements StageAwareController {

    private Stage stage;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }


}
