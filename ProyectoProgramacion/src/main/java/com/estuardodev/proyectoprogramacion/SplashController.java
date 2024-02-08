package com.estuardodev.proyectoprogramacion;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

public class SplashController {

    @FXML
    private ProgressBar pgBar;

    @FXML
    public void initialize() {
        // Configuración de la animación
        Duration duration = Duration.seconds(3); // Duración total de la animación (3 segundos)
        double finalValue = 1.0; // Valor final (100%)

        // Creación del timeline
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> {
                    // Acción al inicio de la animación (valor inicial de la barra de progreso)
                    pgBar.setProgress(0);
                }),
                new KeyFrame(duration, event -> {
                    // Acción al final de la animación (valor final de la barra de progreso)
                    pgBar.setProgress(finalValue);
                })
        );

        // Ejecutar la animación
        timeline.play();
    }
}