package com.estuardodev.proyectoprogramacion.Admin;

import com.estuardodev.proyectoprogramacion.StageAwareController;
import javafx.stage.Stage;

public class DashboardAdmin implements StageAwareController {

    private Stage stage;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
