package com.estuardodev.proyectoprogramacion;

import com.estuardodev.proyectoprogramacion.Clases.CodigoTelefono;
import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Usuario implements Serializable {

    protected Integer id;
    protected String dpi;
    protected String nombre;
    protected String telefono;
    protected int cantidadPrestamo;
    protected String vencimientoPrestamo;
    protected String address;
    protected int multasPendientes;
    protected double totalDeudasPendientes;
    protected int codigo;
    protected String username;
    protected String password;
    protected boolean admin;
    protected boolean activo;
    protected String correo;
    protected String resend;
    protected boolean recopilar;
    protected String librosPrestados;

    public Usuario(){}

    public Usuario(int _id, String _nombre, String _dpi, int _codigo, String _telefono,
                   String _username, String _correo, String _password, String _address,
                   boolean is_admin) {
        id = _id;
        nombre = _nombre;
        dpi = _dpi;
        codigo = _codigo;
        telefono = _telefono;
        username = _username;
        correo = _correo;
        password = _password;
        address = _address;
        admin = is_admin;
    }
    public Usuario(int id, String identificador, String nombre, String telefono, int cantidadPrestamo, String vencimientoPrestamo,
                   String direccion, int multasPendientes, double totalDeudasPendientes, int codigoTelefono,
                   String username, String password, boolean esAdmin, boolean activo, String email, String resend,
                   boolean recopilar, String librosPrestados) {
        this.id = id;
        this.dpi = identificador;
        this.nombre = nombre;
        this.telefono = telefono;
        this.cantidadPrestamo = cantidadPrestamo;
        this.vencimientoPrestamo = vencimientoPrestamo;
        this.address = direccion;
        this.multasPendientes = multasPendientes;
        this.totalDeudasPendientes = totalDeudasPendientes;
        this.codigo = codigoTelefono;
        this.username = username;
        this.password = password;
        this.admin = esAdmin;
        this.activo = activo;
        this.correo = email;
        this.resend = resend;
        this.recopilar = recopilar;
        this.librosPrestados = librosPrestados;
    }

    public int CrearUsuario() {
        /*
         * @return Código 0 = Usuario agregado correctamente
         * @return Código 1 = Campos faltantes
         * @return Codigo 2 = Usuario ya existe
         * @return Código 3 = Teléfono no existe
         * @return Código 9 = Error
         * */

        try {
            if (nombre.isBlank() || telefono.isBlank() || address.isBlank()
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
                        String id_code = Integer.toString(codigo);

                        String query = "INSERT INTO usuario (nombre, identificador, telefono, direccion, codigo_telefono, username, password, es_admin, activo, email) VALUES ('" +
                                nombre + "', '" + dpi + "', '" + telefono + "', '" + address + "', '" + id_code + "', '" + username + "', '" + password + "', true, true, '" + correo + "')";
                        DbConexion.ejecutarInsercion(query);

                        return 0;
                    }else {
                        CodigoTelefono ct = new CodigoTelefono(0, codigo);
                        ResultSet rs = ct.getIdTelefono();

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

    public int CrearAdminInAdmin(){
        String verUsername = "SELECT * FROM usuario WHERE username = ?";
        String verCorreo = "SELECT * FROM usuario WHERE email = ?";
        String verDPI = "SELECT * FROM usuario WHERE identificador = ?";

        try {
            PreparedStatement st = DbConexion.Conexion().prepareStatement(verUsername);
            PreparedStatement st1 = DbConexion.Conexion().prepareStatement(verCorreo);
            PreparedStatement st2 = DbConexion.Conexion().prepareStatement(verDPI);

            st.setString(1, getUsername());
            st1.setString(1, getCorreo());
            st2.setString(1, getDpi());

            ResultSet rs = st.executeQuery();
            ResultSet rs1 = st1.executeQuery();
            ResultSet rs2 = st2.executeQuery();

            if (rs.next()) {
                System.out.println("Existe username");
                return 5; // Username Existe
            }
            if (rs1.next()) {
                System.out.println("Existe Correo");
                return 6; // Correo Existe
            }
            if (rs2.next()) {
                System.out.println("Existe dpi");
                return 7; // Dpi Existe
            }

            String user = "INSERT INTO usuario(identificador, nombre, telefono, direccion," +
                    "codigo_telefono, username, password, es_admin, activo, email, recopilar) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = DbConexion.Conexion().prepareStatement(user);
            ps.setString(1, getDpi());
            ps.setString(2, getNombre());
            ps.setString(3, getTelefono());
            ps.setString(4, getAddress());
            ps.setInt(5, getCodigo());
            ps.setString(6, getUsername());
            ps.setString(7, getPassword());
            ps.setBoolean(8, getAdmin());
            ps.setBoolean(9, isActivo());
            ps.setString(10, getCorreo());
            ps.setBoolean(11, isRecopilar());

            ps.executeUpdate();
            return 0;


        }catch (SQLException sql){
            sql.printStackTrace();
        }

        return 9;
    }

    public boolean getAdmin(){
        return admin;
    }

    public int ActualizarUsuario(){
        /*
         * @return Código 0 = Usuario actualizado correctamente
         * @return Código 9 = Error
         * */
        if(id == null){
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
                return 0;
            }
            return 9;
        }catch (SQLException e){
            e.printStackTrace();
            return 9;
        }
    }

    public void EliminarUsuario(){
        DbConexion.eliminarRegistro("usuario", "id", "", Integer.toString(id), "");
    }

    public ArrayList<Usuario> obtenerTodo(){

        Statement st = null;
        ResultSet rs = null;
        ArrayList<Usuario> usuarios = new ArrayList<>();

        try {
            String sql = "SELECT * FROM usuario";
            st = DbConexion.Conexion().createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setDpi(rs.getString(2));
                usuario.setNombre(rs.getString(3));
                usuario.setTelefono(rs.getString(4));
                usuario.setAddress(rs.getString(7));
                usuario.setUsername(rs.getString(11));
                usuario.setCorreo(rs.getString(15));

                usuarios.add(usuario);
            }

            return usuarios;
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getCantidadPrestamo() {
        return cantidadPrestamo;
    }

    public void setCantidadPrestamo(int cantidadPrestamo) {
        this.cantidadPrestamo = cantidadPrestamo;
    }

    public String getVencimientoPrestamo() {
        return vencimientoPrestamo;
    }

    public void setVencimientoPrestamo(String vencimientoPrestamo) {
        this.vencimientoPrestamo = vencimientoPrestamo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMultasPendientes() {
        return multasPendientes;
    }

    public void setMultasPendientes(int multasPendientes) {
        this.multasPendientes = multasPendientes;
    }

    public double getTotalDeudasPendientes() {
        return totalDeudasPendientes;
    }

    public void setTotalDeudasPendientes(double totalDeudasPendientes) {
        this.totalDeudasPendientes = totalDeudasPendientes;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getResend() {
        return resend;
    }

    public void setResend(String resend) {
        this.resend = resend;
    }

    public boolean isRecopilar() {
        return recopilar;
    }

    public void setRecopilar(boolean recopilar) {
        this.recopilar = recopilar;
    }

    public String getLibrosPrestados() {
        return librosPrestados;
    }

    public void setLibrosPrestados(String librosPrestados) {
        this.librosPrestados = librosPrestados;
    }
}
