package com.estuardodev.proyectoprogramacion.Usuarios;

import com.estuardodev.proyectoprogramacion.Admin.AdminMetodos;
import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;
import com.estuardodev.proyectoprogramacion.ProyectoApplication;
import com.estuardodev.proyectoprogramacion.StageAwareController;
import com.estuardodev.proyectoprogramacion.Utilidades.Encrypt;
import com.estuardodev.proyectoprogramacion.Utilidades.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class    DashboardUser implements StageAwareController, Initializable {

    private Stage stage;
    // Instancia de Clases
    AdminMetodos adminMetodos = new AdminMetodos();
    ResultSet userPerfil = adminMetodos.perfilUser();
    PerfilUsuarios pU = new PerfilUsuarios();
    ProyectoApplication pa = new ProyectoApplication();

    Utils utils = new Utils();

    // Perfil
    @FXML
    private Button btnCerrarSesion, PerfilGuardar, idPerfilEditar;
    @FXML
    private TextField PerfilNombre, PerfilDpi, PerfilEmail, PerfilTelefono,PerfilPrestamos,
            PerfilMultas, PerfilMultasTotal, PerfilPassOld, PerfilNewPass, PerfilNewPassRepeat,
            PerfilAddress;
    @FXML
    private ComboBox<String> PerfilCode;
    @FXML
    private DatePicker PerfilPrestamosDate;
    @FXML
    private Label idLabel, Exitosamente;

    // Historial
    @FXML
    private ListView<String> HistorialList;

    // Prestar Libro
    @FXML
    private ComboBox<String> PrestarCombo;
    @FXML
    private Button PrestarPrestar;
    @FXML
    private TextField PrestarNombre, PrestarAutor, PrestarFecha;
    @FXML
    private CheckBox PrestarCheck;
    @FXML
    private Label PrestarId, PrestarDispoible;

    // Devolver Libro
    @FXML
    private ComboBox<String> DevolverCombo;
    @FXML
    private Button DevolverDevolver;
    @FXML
    private TextField DevolverNombre, DevolverFecha, DevolverAutor;
    @FXML
    private Label DevolverLabel, DevolverId;

    // Pagar Multa
    @FXML
    private RadioButton PagarCompleto, PagarParcial;
    @FXML
    private TextField PagarPendientes, PagarTotal, PagarMonto;
    @FXML
    private Button PagarPagar;
    @FXML
    private Label PagarCantidad, PagarPagado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        verificarMultas();
        MultasCargar(PagarPagado, PagarCantidad, PagarPendientes, PagarTotal, PerfilMultas, PerfilMultasTotal);

        pU.EstablecerPerfil(idLabel,PerfilNombre, PerfilDpi, PerfilEmail,PerfilTelefono, PerfilAddress,
                PerfilPrestamos, PerfilMultas, PerfilMultasTotal, PerfilCode, PerfilPrestamosDate);

        MultasCargar(PagarPagado, PagarCantidad, PagarPendientes, PagarTotal, PerfilMultas, PerfilMultasTotal);
        adminMetodos.cargarListView(HistorialList, "accion", "historial_acciones", idLabel.getText());
        adminMetodos.comboBoxConsultar("titulo", "libro", PrestarCombo);

        CargarDevolverBox(DevolverCombo, DevolverLabel);
    }
    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Perfil
    @FXML
    protected void btnCerrarSesion(){
        File file = new File("id.txt");
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("");

            pa.mostrarVista(stage, "Login-view.fxml");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    protected void PerfilEditar(){

        if (PerfilGuardar.isDisable()){
            PerfilNombre.setEditable(true);
            PerfilDpi.setEditable(true);
            PerfilEmail.setEditable(true);
            PerfilCode.setDisable(false);
            PerfilTelefono.setEditable(true);
            PerfilAddress.setEditable(true);
            PerfilGuardar.setDisable(false);
            idPerfilEditar.setText("Cancelar");
        }else{
            PerfilNombre.setEditable(false);
            PerfilDpi.setEditable(false);
            PerfilEmail.setEditable(false);
            PerfilTelefono.setEditable(false);
            PerfilCode.setDisable(true);
            PerfilAddress.setEditable(false);
            PerfilGuardar.setDisable(true);
            idPerfilEditar.setText("Editar");
        }
    }
    @FXML
    protected void PerfilGuardar(){
        String id = idLabel.getText();
        String name = PerfilNombre.getText();
        String dpi = PerfilDpi.getText();
        String correo = PerfilEmail.getText();
        String code = PerfilCode.getValue();
        String number = PerfilTelefono.getText();
        String address = PerfilAddress.getText();
        System.out.println(code);

        if(code==null){
            code = PerfilCode.getPromptText();
        }

        String query_code = "SELECT id FROM codigotelefono WHERE codigo = " + code;
        ResultSet rs = DbConexion.ConsultaSQL(query_code);
        try {
            if (rs.next()){
                String codigo = rs.getString("id");
                String query_update = "UPDATE usuario SET identificador = '" + dpi +
                        "', nombre = '" + name + "', telefono = '" + number + "', direccion = '" + address +
                        "', codigo_telefono = '" + codigo + "', email = '" + correo + "' WHERE id = '" + id + "'";
                System.out.println("Llego aca");
                DbConexion.ejecutarUpdate(query_update);
                Exitosamente.setVisible(true);
                PerfilEditar();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }


    }
    @FXML
    protected void PerfilChangePass(){
        String pass = PerfilPassOld.getText();
        String newPass1 = PerfilNewPass.getText();
        String newPass2 = PerfilNewPassRepeat.getText();
        String user = idLabel.getText();
        String query_pass = "SELECT password FROM usuario WHERE id  = '" + user +"'";
        ResultSet rs = DbConexion.ConsultaSQL(query_pass);
        try {
            if (rs.next()){
                pass = Encrypt.getSHA256(pass);
                if(pass.equals(rs.getString("password")) && newPass1.equals(newPass2) &&
                    !newPass1.isBlank()){
                    String passNew = Encrypt.getSHA256(newPass1);
                    String newPassword_query = "UPDATE usuario SET password = '"+passNew+"' WHERE id = '"+user+"'";
                    DbConexion.ejecutarUpdate(newPassword_query);
                    Exitosamente.setText("Contraseña cambiada.");
                    PerfilPassOld.clear();
                    PerfilNewPass.clear();
                    PerfilNewPassRepeat.clear();
                }else if(pass.equals(rs.getString("password"))){
                    Exitosamente.setText("La nueva contraseña es incorrecta.");
                    Exitosamente.setVisible(true);
                    Exitosamente.setTextFill(Color.YELLOWGREEN);
                }else{
                    Exitosamente.setText("La contraseña es incorrecta.");
                    Exitosamente.setVisible(true);
                    Exitosamente.setTextFill(Color.RED);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    // Historial
    @FXML
    protected void HistorialActualizar(){
        adminMetodos.cargarListView(HistorialList, "accion", "historial_acciones", idLabel.getText());
    }

    @FXML
    protected void HistorialExportar() {
        utils.ExportarHistorial(HistorialList);

    }

    // Prestar Libro
    @FXML
    protected void PrestarConsultar(){
        String libro = PrestarCombo.getValue();
        String query = "SELECT * FROM libro WHERE titulo = '"+libro+"'";

        ResultSet rs = DbConexion.ConsultaSQL(query);
        try {
            if (rs.next()){
                PrestarNombre.setText(rs.getString("titulo"));
                ResultSet rsAutor = DbConexion.ConsultaSQL("SELECT nombre FROM autor WHERE id = '"+rs.getString("fkidautor")+"'");
                if (rsAutor.next()) {
                    PrestarAutor.setText(rsAutor.getString("nombre"));
                }
                PrestarFecha.setText(rs.getString("fecha_publicacion"));
                PrestarId.setText(rs.getString("id"));
                PrestarNombre.setDisable(false);
                PrestarAutor.setDisable(false);
                PrestarFecha.setDisable(false);
                PrestarCheck.setDisable(false);
                PrestarPrestar.setDisable(false);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    @FXML
    protected void PrestarPrestar(){
        String id = PrestarId.getText();
        String query = "SELECT cantidad_stock FROM libro WHERE id = '"+id+"'";
        ResultSet rs = DbConexion.ConsultaSQL(query);
        boolean check = PrestarCheck.isSelected();
        int multa;
        try {
            multa = Integer.parseInt(userPerfil.getString("total_deudas_pendientes"));
        }catch (Exception e){
            multa = 0;
        }
        try {
            if (rs.next() && check && multa == 0){
                int cantidad = Integer.parseInt(rs.getString("cantidad_stock"));
                if (cantidad <= 0){
                    PrestarDispoible.setVisible(true);
                }else {
                    PrestarDispoible.setVisible(false);
                    String idUser = idLabel.getText();
                    String query2 = "SELECT cantidad_prestamo, librosprestados, vencimiento_prestamo" +
                            " FROM usuario WHERE id = '"+idUser+"'";
                    ResultSet user = DbConexion.ConsultaSQL(query2);
                    if (user.next()){
                        int prestamos;
                        try {
                            prestamos = Integer.parseInt(user.getString("cantidad_prestamo"));
                        }catch (Exception e){
                            prestamos = 0;
                        }

                        if (prestamos > 0){
                            PrestarDispoible.setVisible(true);
                            PrestarDispoible.setText("Tienes prestamos pendientes.");
                        }else{
                            String fecha = AdminMetodos.obtenerFechaActual(false, true);
                            String idLibro = PrestarId.getText();
                            String librosPrestados = user.getString("librosprestados");
                            String[] libros = new String[0];

                            if (librosPrestados != null && !librosPrestados.isEmpty()) {
                                libros = librosPrestados.split(",\\s*");
                            }

                            String[] nuevosLibros = new String[libros.length + 1];
                            System.arraycopy(libros, 0, nuevosLibros, 0, libros.length);
                            nuevosLibros[libros.length] = idLibro;
                            libros = nuevosLibros;
                            String librosConcatenados = String.join(", ", libros);
                            String query3 = "UPDATE usuario SET cantidad_prestamo = '"+(prestamos+1)+"'," +
                                    "librosprestados = '"+librosConcatenados+"'," +
                                    "vencimiento_prestamo='"+fecha+"," +
                                    "ultimopago = '"+fecha+"'' WHERE id='"+idUser+"'";
                            DbConexion.ejecutarUpdate(query3);
                            cantidad -= 1;
                            String query4 = "UPDATE libro SET cantidad_stock = '"+cantidad+"' WHERE id = '"+idLibro+"'";
                            DbConexion.ejecutarUpdate(query4);
                            PrestarDispoible.setVisible(true);
                            PrestarDispoible.setText("Libro prestado");
                            PrestarDispoible.setTextFill(Color.YELLOWGREEN);
                            PrestarCheck.setSelected(false);
                            PrestarNombre.clear();
                            PrestarAutor.clear();
                            PrestarFecha.clear();
                            PrestarId.setText("");
                            PrestarNombre.clear();
                            PrestarNombre.setDisable(true);
                            PrestarAutor.setDisable(true);
                            PrestarFecha.setDisable(true);
                            PrestarCheck.setDisable(true);
                            PrestarPrestar.setDisable(true);
                            CargarDevolverBox(DevolverCombo, DevolverLabel);
                            adminMetodos.GuardarAccion("un", user, "prestamo");

                        }
                    }
                }
            }else{
                if (multa != 0){
                    PrestarDispoible.setVisible(true);
                    PrestarDispoible.setText("Tienes multas pendientes");
                    PrestarDispoible.setTextFill(Color.YELLOWGREEN);
                }else{
                    PrestarDispoible.setVisible(true);
                    PrestarDispoible.setText("Acepta los términos");
                    PrestarDispoible.setTextFill(Color.YELLOWGREEN);
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Devolver Libro
    @FXML
    protected void DevolverConsultar(){
        String libro = DevolverCombo.getValue();
        String query = "SELECT * FROM libro WHERE titulo = '"+libro+"'";

        ResultSet rs = DbConexion.ConsultaSQL(query);
        try {
            if (rs.next()){
                DevolverNombre.setText(rs.getString("titulo"));
                ResultSet rsAutor = DbConexion.ConsultaSQL("SELECT nombre FROM autor WHERE id = '"+rs.getString("fkidautor")+"'");
                if (rsAutor.next()) {
                    DevolverAutor.setText(rsAutor.getString("nombre"));
                }
                DevolverFecha.setText(rs.getString("fecha_publicacion"));
                DevolverId.setText(rs.getString("id"));
                DevolverNombre.setDisable(false);
                DevolverAutor.setDisable(false);
                DevolverFecha.setDisable(false);
                DevolverDevolver.setDisable(false);
                DevolverLabel.setVisible(false);
                verificarMultas();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    @FXML
    protected void DevolverDevolver(){
        String idLibro = DevolverId.getText();
        try {
            if (true){
                String query = "SELECT * FROM usuario WHERE id = '"+idLabel.getText()+"'";
                ResultSet rs = DbConexion.ConsultaSQL(query);
                if (rs.next()) {
                    String librosPrestados = rs.getString("librosprestados");
                    String[] libros = new String[0];
                    if (librosPrestados != null && !librosPrestados.isEmpty()) {
                        libros = librosPrestados.split(",\\s*");

                        // Crear una lista temporal para almacenar los libros que no sean el idLibro
                        List<String> nuevosLibros = new ArrayList<>();
                        for (String libro : libros) {
                            if (!libro.equals(idLibro)) {
                                nuevosLibros.add(libro);
                            }
                        }
                        String[] librosNoIdLibro = nuevosLibros.toArray(new String[0]);
                        String librosConcatenados = String.join(", ", librosNoIdLibro);

                        ResultSet query_libros = DbConexion.ConsultaSQL("SELECT * FROM libro WHERE id = '"+idLibro+"'");
                        query_libros.next();
                        int cantidad = Integer.parseInt(query_libros.getString("cantidad_stock"));
                        cantidad += 1;
                        String updateLibro = "UPDATE libro SET cantidad_stock = '"+cantidad+"' WHERE id= '"+idLibro+"'";
                        DbConexion.ejecutarUpdate(updateLibro);
                        int cantidad_prestados = Integer.parseInt(rs.getString("cantidad_prestamo"));
                        cantidad_prestados -= 1;
                        String updateUser = "UPDATE usuario SET librosprestados = '"+librosConcatenados+"'," +
                                "vencimiento_prestamo = NULL, cantidad_prestamo = '"+cantidad_prestados+"'  WHERE id= '"+idLabel.getText()+"'";
                        DbConexion.ejecutarUpdate(updateUser);
                        DevolverLabel.setVisible(true);
                        DevolverFecha.clear();
                        DevolverNombre.clear();
                        DevolverAutor.clear();
                        DevolverCombo.getSelectionModel().clearSelection();
                        DevolverId.setText("");
                        DevolverNombre.setDisable(true);
                        DevolverAutor.setDisable(true);
                        DevolverFecha.setDisable(true);
                        DevolverDevolver.setDisable(true);
                        DevolverCombo.getItems().clear();
                        CargarDevolverBox(DevolverCombo, DevolverLabel);
                        adminMetodos.GuardarAccion("una", userPerfil, "devolución");

                    }else{
                        DevolverLabel.setVisible(false);
                        DevolverFecha.clear();
                        DevolverNombre.clear();
                        DevolverAutor.clear();
                        DevolverCombo.getSelectionModel().clearSelection();
                        DevolverId.setText("");
                        DevolverNombre.setDisable(true);
                        DevolverAutor.setDisable(true);
                        DevolverFecha.setDisable(true);
                        DevolverDevolver.setDisable(true);
                        CargarDevolverBox(DevolverCombo, DevolverLabel);
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    @FXML
    private void CargarDevolverBox(ComboBox<String> cb, Label info){
        try {

            String query = "SELECT * FROM usuario WHERE id = '"+Integer.parseInt(idLabel.getText())+"'";
            ResultSet rs = DbConexion.ConsultaSQL(query);
            if (rs.next()){
                String librosPrestados = rs.getString("librosprestados");
                String[] libros = new String[0];
                if (librosPrestados != null && !librosPrestados.isEmpty()) {
                    libros = librosPrestados.split(",\\s*");
                    int i = 0;
                    while (i < libros.length){
                        String idLibro = libros[i];
                        String queryLibro = "SELECT * FROM libro WHERE id = '"+idLibro+"'";
                        ResultSet rsLibro = DbConexion.ConsultaSQL(queryLibro);
                        rsLibro.next();
                        cb.getItems().add(rsLibro.getString("titulo"));
                        i += 1;
                    }
                }else{
                    info.setText("NO TIENES LIBROS POR DEVOLVER.");
                    info.setVisible(true);
                    info.setTextFill(Color.YELLOWGREEN);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Pagar Multa
    @FXML
    protected void PagarParcial(){
        PagarCompleto.setSelected(false);
        PagarMonto.setDisable(false);
        PagarPagar.setDisable(false);
        PagarParcial.setSelected(true);
    }
    @FXML
    protected void PagarCompleto(){
        PagarCompleto.setSelected(true);
        PagarParcial.setSelected(false);
        PagarMonto.setDisable(true);
        PagarPagar.setDisable(false);
    }
    @FXML
    protected void PagarPagar() {
        String query = "SELECT * FROM usuario WHERE id = '"+idLabel.getText()+"'";
        ResultSet perfil = DbConexion.ConsultaSQL(query);
        try{
            if (PagarCompleto.isSelected()){
                 if (perfil.next()){
                        String update = "UPDATE usuario SET " +
                                "total_deudas_pendientes = '0'," +
                                "multas_pendientes = 0,"  +
                                "ultimopago = NULL," +
                                "librosprestados = '' WHERE id = '"+perfil.getString("id")+"'";
                        DbConexion.ejecutarUpdate(update);
                        PagarPagado.setVisible(true);
                        PagarPagado.setText("Pagado totalmente");
                        PagarPagado.setTextFill(Color.YELLOWGREEN);
                     MultasCargar(PagarPagado, PagarCantidad, PagarPendientes, PagarTotal, PerfilMultas, PerfilMultasTotal);
                 }else{
                     System.out.println("No hay perfil");
                 }
            }else if(PagarParcial.isSelected()){
                if (perfil.next()){
                    int pago = Integer.parseInt(PagarMonto.getText());
                    int pagar = Integer.parseInt(perfil.getString("total_deudas_pendientes"));

                    if (pagar < pago){
                        PagarPagado.setVisible(true);
                        PagarPagado.setText("Estás pagando de más");
                        PagarPagado.setTextFill(Color.RED);
                    }else if(pago == pagar){
                        String update = "UPDATE usuario SET " +
                                "total_deudas_pendientes = '0'," +
                                "multas_pendientes = 0," +
                                "ultimopago = NULL," +
                                "librosprestados = '' WHERE id = '"+perfil.getString("id")+"'";
                        DbConexion.ejecutarUpdate(update);
                        PagarPagado.setVisible(true);
                        PagarPagado.setText("Pagado totalmente");
                        PagarPagado.setTextFill(Color.YELLOWGREEN);
                        MultasCargar(PagarPagado, PagarCantidad, PagarPendientes, PagarTotal, PerfilMultas, PerfilMultasTotal);

                    }
                    else{

                        int parcial = pagar - pago;
                        String hoy = AdminMetodos.obtenerFechaActual(false, false);
                        String update = "UPDATE usuario SET " +
                                "multas_pendientes = '1'," +
                                "total_deudas_pendientes = '"+parcial+"'," +
                                "cantidad_prestamo = 1, " +
                                "ultimopago = '"+hoy+"' WHERE id = '"+perfil.getString("id")+"'";
                        DbConexion.ejecutarUpdate(update);
                        MultasCargar(PagarPagado, PagarCantidad, PagarPendientes, PagarTotal, PerfilMultas, PerfilMultasTotal);
                        PagarPagado.setVisible(true);
                        PagarPagado.setText("Pagado parcialmente");
                        PagarPagado.setTextFill(Color.YELLOWGREEN);
                    }
                }
            }else{
                PagarPagado.setVisible(true);
                PagarPagado.setText("Seleccione una opción");
                PagarPagado.setTextFill(Color.RED);
            }
            adminMetodos.GuardarAccion("un", userPerfil, "pago");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void MultasCargar(Label lb, Label total, TextField pendientes, TextField deuda, TextField PerfilLabelMultas, TextField PerfilLabelDeudas){
        ResultSet perfil = adminMetodos.perfilUser();
        try {
            if(perfil.next()){
                String multas = perfil.getString("multas_pendientes");
                if (multas == null || multas.isBlank()){
                    lb.setVisible(true);
                    lb.setText("No tienes multas");
                    lb.setTextFill(Color.YELLOWGREEN);
                    total.setText("0");
                    PagarParcial.setDisable(true);
                    PagarCompleto.setDisable(true);
                    PagarPagar.setDisable(true);
                    PagarMonto.setDisable(true);
                    PagarPendientes.setText("No hay multas");
                    PagarTotal.setText("No hay deuda");
                    PerfilLabelMultas.setText("No hay multas");
                    PerfilLabelDeudas.setText("No hay deuda");

                }else{
                    pendientes.setText(multas);
                    deuda.setText(perfil.getString("total_deudas_pendientes"));
                    total.setText(perfil.getString("total_deudas_pendientes"));
                    PerfilLabelMultas.setText(perfil.getString("multas_pendientes"));
                    PerfilLabelDeudas.setText(perfil.getString("total_deudas_pendientes"));
                    PagarParcial.setDisable(false);
                    PagarCompleto.setDisable(false);
                    PagarPagar.setDisable(true);
                    lb.setVisible(false);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void verificarMultas() {
        try {
            ResultSet usuario = adminMetodos.perfilUser();
            if (usuario.next()) {
                String date = usuario.getString("ultimopago");
                if (date != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {


                        java.util.Date fechaVencimiento = sdf.parse(date);
                        Date fechaActual = new Date(System.currentTimeMillis());
                        long diferenciaMilisegundos = fechaActual.getTime() - fechaVencimiento.getTime();
                        long diasPasados = TimeUnit.DAYS.convert(diferenciaMilisegundos, TimeUnit.MILLISECONDS);

                        int total = usuario.getInt("total_deudas_pendientes");

                        if (total > 0 && diasPasados == 0) {
                            String query = "UPDATE usuario SET total_deudas_pendientes = '" + total + "', " +
                                    "multas_pendientes = '1' WHERE id = '" + usuario.getString("id") + "'";
                            DbConexion.ejecutarUpdate(query);
                        } else if (total > 0 && diasPasados == 1) {
                                total += 2;
                                String query = "UPDATE usuario SET total_deudas_pendientes = '" + total + "', " +
                                        "multas_pendientes = '1' WHERE id = '" + usuario.getString("id") + "'";
                                DbConexion.ejecutarUpdate(query);
                        } else if (diasPasados > 0){
                            total = (int) (diasPasados * 2);
                            String query = "UPDATE usuario SET total_deudas_pendientes = '" + total + "', " +
                                    "multas_pendientes = '1' WHERE id = '" + usuario.getString("id") + "'";
                            DbConexion.ejecutarUpdate(query);

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("No hay usuario");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
