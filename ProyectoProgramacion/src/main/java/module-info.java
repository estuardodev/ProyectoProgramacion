module com.estuardodev.proyectoprogramacion {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires hibernate.jpa;
    requires resend.java;
    requires com.google.gson;

    opens com.estuardodev.proyectoprogramacion to javafx.fxml;
    exports com.estuardodev.proyectoprogramacion;
    exports com.estuardodev.proyectoprogramacion.DataBase;
    exports com.estuardodev.proyectoprogramacion.Admin;
    exports com.estuardodev.proyectoprogramacion.Usuarios;
    opens com.estuardodev.proyectoprogramacion.DataBase to javafx.fxml;
    opens com.estuardodev.proyectoprogramacion.Admin to javafx.fxml;
    opens com.estuardodev.proyectoprogramacion.Usuarios to javafx.fxml;
}