module com.estuardodev.proyectoprogramacion {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.estuardodev.proyectoprogramacion to javafx.fxml;
    exports com.estuardodev.proyectoprogramacion;
}