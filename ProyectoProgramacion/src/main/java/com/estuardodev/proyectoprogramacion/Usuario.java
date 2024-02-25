package com.estuardodev.proyectoprogramacion;

import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuario extends User {

    @Override
    public int CrearUsuario() {
        /*
         * @return Código 0 = Usuario agregado correctamente
         * @return Código 1 = Campos faltantes
         * @return Codigo 2 = Usuario ya existe
         * @return Código 3 = Teléfono no existe
         * @return Código 9 = Error
         * */

        try {
            if (nombre.isBlank() || telefono.isBlank() || address.isBlank() || codigo.isBlank()
                    || username.isBlank() || password.isBlank() || dpi.isBlank() || dpi.length() != 13) {
                return 1;
            } else {
                String verificar_user = "SELECT * FROM usuario WHERE username = '" + username + "'";
                String verificar_dpi = "SELECT * FROM usuario WHERE identificador = '" + dpi + "'";
                String verificar_correo = "SELECT * FROM usuario WHERE identificador = '" + correo + "'";

                ResultSet user_none = DbConexion.ConsultaSQL(verificar_user);
                ResultSet dpi_none = DbConexion.ConsultaSQL(verificar_dpi);
                ResultSet email_none = DbConexion.ConsultaSQL(verificar_correo);

                if (user_none.next() || dpi_none.next() || email_none.next()) {
                    return 2;
                } else {
                    if (admin){
                        String id_code = codigo;

                        String query = "INSERT INTO usuario (nombre, identificador, telefono, direccion, codigo_telefono, username, password, es_admin, activo, email) VALUES ('" +
                                nombre + "', '" + dpi + "', '" + telefono + "', '" + address + "', '" + id_code + "', '" + username + "', '" + password + "', true, true, '" + correo + "')";
                        DbConexion.ejecutarInsercion(query);

                        return 0;
                    }else {
                        String id_code_query = "SELECT id FROM codigotelefono WHERE codigo = '" + codigo + "'";
                        ResultSet rs = DbConexion.ConsultaSQL(id_code_query);

                        if (rs.next()) {
                            String id_code = rs.getString("id");

                            String query = "INSERT INTO usuario (nombre, identificador, telefono, direccion, codigo_telefono, username, password, activo, email) VALUES ('" +
                                    nombre + "', '" + dpi + "', '" + telefono + "', '" + address + "', '" + id_code + "', '" + username + "', '" + password + "', true, '" + correo + "')";
                            DbConexion.ejecutarInsercion(query);

                            return 0;

                        } else {
                            return 3;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            return 9;
        }
    }

    @Override
    public int ActualizarUsuario(){
        /*
         * @return Código 0 = Usuario actualizado correctamente
         * @return Código 9 = Error
         * */
        if(id==null || id.isBlank()){
            return 9;
        }
        String query_code = "SELECT id FROM codigotelefono WHERE codigo = " + codigo;
        ResultSet rs = DbConexion.ConsultaSQL(query_code);
        try {
            if (rs.next()){
                String code = rs.getString("id");
                String query_update;
                if (admin){
                    query_update = "UPDATE usuario SET identificador = '" + dpi +
                            "', nombre = '" + nombre + "', telefono = '" + telefono + "', username = '" + username + "'," +
                            " direccion = '" + address +"', codigo_telefono = '" + code + "', email = '" + correo + "' " +
                            "WHERE id = '" + id + "'";
                }else{
                    query_update = "UPDATE usuario SET identificador = '" + dpi +
                            "', nombre = '" + nombre + "', telefono = '" + telefono + "', direccion = '" + address +
                            "', codigo_telefono = '" + code + "', email = '" + correo + "' WHERE id = '" + id + "'";
                }

                DbConexion.ejecutarUpdate(query_update);
                return 1;
            }
            return 9;
        }catch (SQLException e){
            e.printStackTrace();
            return 9;
        }
    }
}
