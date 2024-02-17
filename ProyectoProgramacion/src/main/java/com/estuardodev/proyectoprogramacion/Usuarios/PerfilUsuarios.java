package com.estuardodev.proyectoprogramacion.Usuarios;

import com.estuardodev.proyectoprogramacion.Admin.AdminMetodos;
import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PerfilUsuarios {
    AdminMetodos adminMetodos = new AdminMetodos();
    ResultSet user = adminMetodos.perfilUser();

    protected void EstablecerPerfil(Label id, TextField name, TextField dpi, TextField email,
                                    TextField number, TextField address, TextField pretamo,
                                    TextField multas, TextField deuda, ComboBox<String> cb,
                                    DatePicker date){
        try {
            if (user.next()){
                id.setText(user.getString("id"));
                name.setText(user.getString("nombre"));
                dpi.setText(user.getString("identificador"));
                email.setText(user.getString("email"));
                number.setText(user.getString("telefono"));
                address.setText(user.getString("direccion"));
                adminMetodos.comboBoxConsultar("codigo","codigotelefono",cb);
                String query_fast = "SELECT codigo FROM codigotelefono WHERE id = "+user.getString("codigo_telefono");
                ResultSet rs = DbConexion.ConsultaSQL(query_fast);
                rs.next();
                cb.setPromptText(rs.getString("codigo"));

                if (user.getString("multas_pendientes") == null && user.getString("total_deudas_pendientes") == null){
                    multas.setText("SIN MULTAS");
                    deuda.setText("SIN MULTAS");
                }else{
                    multas.setText(user.getString("multas_pendientes"));
                    deuda.setText(user.getString("total_deudas_pendientes"));
                }
                try{
                    if (user.getString("vencimiento_prestamo") != null){
                        pretamo.setText(user.getString("cantidad_prestamo"));
                        date.setValue(LocalDate.parse(user.getString("vencimiento_prestamo")));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

}
