module com.estuardodev.proyectoprogramacion {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires hibernate.jpa;
    requires resend.java;

    opens com.estuardodev.proyectoprogramacion to javafx.fxml;
    exports com.estuardodev.proyectoprogramacion;
    exports com.estuardodev.proyectoprogramacion.DataBase;
    exports com.estuardodev.proyectoprogramacion.Admin;
    exports com.estuardodev.proyectoprogramacion.Usuarios;
    opens com.estuardodev.proyectoprogramacion.DataBase to javafx.fxml;
}