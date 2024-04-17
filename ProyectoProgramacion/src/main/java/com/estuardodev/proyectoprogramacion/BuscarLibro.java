package com.estuardodev.proyectoprogramacion;

import com.estuardodev.proyectoprogramacion.Admin.AdminMetodos;
import com.estuardodev.proyectoprogramacion.Clases.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class BuscarLibro implements StageAwareController, Initializable {
    Stage stage;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private TableView<Libro> tbView;
    @FXML
    private TableColumn<Libro, String> titulo, isbn, fecha, stock, autor, editorial;

    @FXML
    private TextField buscar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Libro lb = new Libro();
        List<Libro> todos = lb.obtenerTodos();

        ObservableList<Libro> observableList = FXCollections.observableArrayList();
        observableList.addAll(todos);

        tbView.setItems(observableList);
        titulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        fecha.setCellValueFactory(new PropertyValueFactory<>("fechaPublicacion"));
        stock.setCellValueFactory(new PropertyValueFactory<>("cantidadStock"));
        autor.setCellValueFactory(new PropertyValueFactory<>("autorId"));
        editorial.setCellValueFactory(new PropertyValueFactory<>("editorialId"));

        // Funcionalidad para búsqueda
        FilteredList<Libro> filteredList = new FilteredList<>(observableList, b -> true);

        // Evento cuando el valor cambia
        buscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(libro -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String minusculas = newValue.toLowerCase();
                return libro.getTitulo().toLowerCase().contains(minusculas);
            });
        });

        SortedList<Libro> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tbView.comparatorProperty());
        tbView.setItems(sortedList);
    }

    @FXML
    protected void regresar() throws IOException, SQLException {
        AdminMetodos am = new AdminMetodos();
        ResultSet rs = am.perfilUser();
        boolean isAdmin = false;

        if (rs.next()){
            isAdmin = rs.getBoolean("es_admin");
        }

        if (isAdmin){
            ProyectoApplication pa = new ProyectoApplication();
            pa.mostrarVista(stage, "AdminUser.fxml");
        }else{
            ProyectoApplication pa = new ProyectoApplication();
            pa.mostrarVista(stage, "DashboardUser.fxml");
        }
    }
}
