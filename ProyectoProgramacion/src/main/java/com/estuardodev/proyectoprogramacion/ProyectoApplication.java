package com.estuardodev.proyectoprogramacion;

import com.estuardodev.proyectoprogramacion.Utilidades.Utils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class ProyectoApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ProyectoApplication.class.getResource("Splash-view.fxml"));
        stage.setResizable(false);
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Sistema bibliotecario");
        stage.setScene(scene);
        stage.show();

        // Programa una tarea que se ejecutará después de 3 segundos
        TimerTask task = new TimerTask() {
            public void run() {
                // Carga el archivo FXML del Login-view.fxml después de 3 segundos
                Platform.runLater(() -> {
                    File file = new File("Biblioteca/init.txt");
                    try {
                        if (!file.exists()) {
                            mostrarVista(stage, "DatabaseConnect.fxml");
                        } else {
                            mostrarVista(stage, "Login-view.fxml");
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 3000); // Programa la tarea para ejecutarse después de 3000 milisegundos (3 segundos)
    }


    public void mostrarVista(Stage stage, String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Pane pane = fxmlLoader.load();
        Scene scene = new Scene(pane);

        // Obtén el controlador del FXML y establece el Stage
        StageAwareController controller = fxmlLoader.getController();
        controller.setStage(stage);

        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}