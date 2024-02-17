package com.estuardodev.proyectoprogramacion.Admin;

import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;
import com.estuardodev.proyectoprogramacion.StageAwareController;
import com.estuardodev.proyectoprogramacion.Utilidades.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.persistence.Convert;
import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardAdmin implements StageAwareController, Initializable {

    private Stage stage;
    private AdminMetodos am = new AdminMetodos();
    Utils utils = new Utils();

    // Libros
    @FXML
    private ComboBox<String> bxConsultarLibro, cbALibrosAutor, cbALibrosEditorial, autorULibroActualizar,
            editorialULibroActualizar, bxULibroActualizar, cbBorrarLibro;
    @FXML
    private TextField txtConsultarTitulo, txtConsultarFecha, txtConsultarISBN, txtConsultarStock, txtConsultarAutor, txtConsultarEditorial,
            BorrarTituloLibro, BorrarISBNLibro, txtULibroActualizarTitulo, txtULibroActualizarISBN,
            txtULibroActualizarStock, txtALibrosTitulo, txtALibrosISBN, spALibrosCantidad;
    @FXML
    private DatePicker txtALibrosDate, txtDateUpdate;
    @FXML
    private Button btnULibroActualizarSave, btnExportarLibro;
    @FXML
    private Label txtALibrosMensaje, txtBienvenida, labelULibroActualizar, alertBorradoLibro;
    @FXML
    private CheckBox checkAceptoTerminos;

    // Autores
    @FXML
    private ListView<String> listAutores;
    @FXML
    private TextField NombreGuardarAutor, NombreActualizarAutor;
    @FXML
    private ComboBox<String> cbAutorActualizar, cbEliminarAutor;
    @FXML
    private Label msgGuardado, msgUpdateAutor, msgEliminado;
    @FXML
    private CheckBox checkEliminarAutor;

    // Editoriales
    @FXML
    private ListView<String> listEditorial;
    @FXML
    private TextField NombreGuardarEditorial, NombreActualizarEditorial;
    @FXML
    ComboBox<String> cbEditorialActualizar, cbEliminarEditorial;
    @FXML
    private Label msgGuardadoEditorial, msgUpdateEditorial, msgEliminadoEditorial;
    @FXML
    private CheckBox checkEliminarEditorial;

    // Perfil
    @FXML
    private TextField PerfilName, PerfilDpi, PerfilCode, PerfilTelefono, PerfilUsername, PerfilCorreo;
    @FXML
    private CheckBox PerfilRecoleccion;
    @FXML
    private Button PerfilActDes;

    // Historial
    @FXML
    private ListView<String> historialView;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        am.comboBoxConsultar("titulo", "libro", bxConsultarLibro);
        am.comboBoxConsultar("nombre","autor", cbALibrosAutor);
        am.comboBoxConsultar("nombre", "editorial", cbALibrosEditorial);
        am.comboBoxConsultar("titulo", "libro", bxULibroActualizar);
        am.comboBoxConsultar("titulo", "libro", cbBorrarLibro);
        am.comboBoxConsultar("nombre", "autor", cbAutorActualizar);
        am.comboBoxConsultar("nombre", "autor", cbEliminarAutor);
        am.comboBoxConsultar("nombre", "editorial", cbEditorialActualizar);
        am.comboBoxConsultar("nombre", "editorial", cbEliminarEditorial);

        am.cargarListView(listAutores, "nombre", "autor");
        am.cargarListView(listEditorial, "nombre", "editorial");
        am.cargarListView(historialView, "accion", "historial_acciones");

        perfilCargar();
    }

    // Libros
    @FXML
    protected void btnConsultarBuscar(){
        String libro = bxConsultarLibro.getValue();
        String query = "SELECT * FROM libro WHERE titulo = '"+libro+"'";
        try {
            ResultSet rs = DbConexion.ConsultaSQL(query);
            if (rs.next()){
                String editorial = "SELECT nombre FROM editorial WHERE id='"+rs.getString("fkideditorial")+"'";
                String autor = "SELECT nombre FROM autor WHERE id='"+rs.getString("fkidautor")+"'";
                ResultSet reditorial = DbConexion.ConsultaSQL(editorial);
                ResultSet rautor = DbConexion.ConsultaSQL(autor);
                reditorial.next();
                rautor.next();
                txtConsultarTitulo.setText(rs.getString("titulo"));
                txtConsultarFecha.setText(rs.getString("fecha_publicacion"));
                txtConsultarISBN.setText(rs.getString("isbn"));
                txtConsultarStock.setText(rs.getString("cantidad_stock"));
                txtConsultarAutor.setText(rautor.getString("nombre"));
                txtConsultarEditorial.setText(reditorial.getString("nombre"));
                btnExportarLibro.setDisable(false);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    @FXML
    protected void btnALibrosGuardar(){
        String titulo = txtALibrosTitulo.getText();
        Date fecha = Date.valueOf(txtALibrosDate.getValue());
        String isbn = txtALibrosISBN.getText();
        int cantidad = Integer.parseInt(spALibrosCantidad.getText());
        String autor = cbALibrosAutor.getValue();
        String editorial = cbALibrosEditorial.getValue();

        if (!titulo.isEmpty() && !isbn.isEmpty() && cantidad > -1 && autor != null && editorial != null) {
            String query_exist = "SELECT id FROM libro WHERE titulo = '"+titulo+"'";
            String queryAutor = "SELECT id FROM autor WHERE nombre = '"+autor+"'";
            String queryEditorial = "SELECT id FROM editorial WHERE nombre = '"+editorial+"'";
            try {
                ResultSet rs = DbConexion.ConsultaSQL(query_exist);
                ResultSet rAutor = DbConexion.ConsultaSQL(queryAutor);
                ResultSet rEditorial = DbConexion.ConsultaSQL(queryEditorial);
                if(!rs.next() && rAutor.next() &&rEditorial.next()){
                    String query = "INSERT INTO libro (titulo, fecha_publicacion, isbn, cantidad_stock, fkidautor, fkideditorial) " +
                            "VALUES ('"+titulo+"','"+fecha.toLocalDate()+"','"+isbn+"',"+cantidad+","+rAutor.getString("id")+","+rEditorial.getString("id")+")";
                    am.GuardarAccion("una", am.perfilUser(), "insercción");
                    DbConexion.ejecutarInsercion(query);
                    txtALibrosMensaje.setVisible(true);
                    txtALibrosMensaje.setText("Nuevo libro agregado");
                    txtALibrosMensaje.setTextFill(Color.GREEN);
                }else{
                    txtALibrosMensaje.setVisible(true);
                    txtALibrosMensaje.setText("Ese libro ya existe.");
                    txtALibrosMensaje.setTextFill(Color.YELLOW);
                }
            }catch (SQLException e){
                e.printStackTrace();
                txtALibrosMensaje.setVisible(true);
                txtALibrosMensaje.setText("Ocurrio un error");
                txtALibrosMensaje.setTextFill(Color.RED);
            }


        } else{
            txtALibrosMensaje.setVisible(true);
            txtALibrosMensaje.setText("LLena todos los campos por favor.");
            txtALibrosMensaje.setTextFill(Color.YELLOW);
        }

    }
    @FXML
    protected void btnALibrosLimpiar() {
        txtALibrosTitulo.clear();
        txtALibrosDate.setValue(null);
        txtALibrosISBN.clear();
        spALibrosCantidad.clear();
        cbALibrosAutor.getSelectionModel().clearSelection();
        cbALibrosEditorial.getSelectionModel().clearSelection();
    }
    @FXML
    protected void btnULibroActualizar(){
        String libro = bxULibroActualizar.getValue();
        String query = "SELECT * FROM libro WHERE titulo = '"+libro+"'";
        try {
            ResultSet rs = DbConexion.ConsultaSQL(query);
            if (rs.next()){
                String editorial = "SELECT nombre FROM editorial WHERE id='"+rs.getString("fkideditorial")+"'";
                String autor = "SELECT nombre FROM autor WHERE id='"+rs.getString("fkidautor")+"'";
                ResultSet reditorial = DbConexion.ConsultaSQL(editorial);
                ResultSet rautor = DbConexion.ConsultaSQL(autor);
                reditorial.next();
                rautor.next();
                txtULibroActualizarTitulo.setText(rs.getString("titulo"));

                String fechaString = rs.getString("fecha_publicacion");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate fecha = LocalDate.parse(fechaString, formatter);
                txtDateUpdate.setValue(fecha);

                txtULibroActualizarISBN.setText(rs.getString("isbn"));
                txtULibroActualizarStock.setText(rs.getString("cantidad_stock"));
                autorULibroActualizar.setValue(rautor.getString("nombre"));
                editorialULibroActualizar.setValue(reditorial.getString("nombre"));

                txtULibroActualizarTitulo.setEditable(true);
                txtDateUpdate.setEditable(true);
                txtULibroActualizarISBN.setEditable(true);
                txtULibroActualizarStock.setEditable(true);
                autorULibroActualizar.setDisable(false);
                editorialULibroActualizar.setDisable(false);
                btnULibroActualizarSave.setDisable(false);
                am.comboBoxConsultar("nombre", "autor", autorULibroActualizar);
                am.comboBoxConsultar("nombre", "editorial", editorialULibroActualizar);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    @FXML
    protected void btnULibroActualizarSave(){
        String titulo = txtULibroActualizarTitulo.getText();
        Date fecha = Date.valueOf(txtDateUpdate.getValue());
        String isbn = txtULibroActualizarISBN.getText();
        int cantidad = Integer.parseInt(txtULibroActualizarStock.getText());
        String autor = autorULibroActualizar.getValue();
        String editorial = editorialULibroActualizar.getValue();

        if (!titulo.isEmpty() || !isbn.isEmpty() || cantidad > -1 || autor != null | editorial != null) {
            String query_exist = "SELECT id FROM libro WHERE titulo = '" + titulo + "' OR isbn = '" + isbn + "'";

            try {
                ResultSet rs = DbConexion.ConsultaSQL(query_exist);
                if(rs.next()){
                    String query = "UPDATE libro " +
                            "SET titulo = '" + titulo + "', " +
                            "fecha_publicacion = '" + fecha.toLocalDate() + "', " +
                            "isbn = '" + isbn + "', " +
                            "cantidad_stock = " + cantidad + ", " +
                            "fkidautor = " + Utils.obtenerIdAutor(autor) + ", " +
                            "fkideditorial = " + Utils.obtenerIdEditorial(editorial) + " " +
                            "WHERE id = " + rs.getString("id");
                    am.GuardarAccion("una", am.perfilUser(), "actualización");
                    DbConexion.ejecutarUpdate(query);
                    labelULibroActualizar.setVisible(true);
                    labelULibroActualizar.setText("Libro actualizado");
                    labelULibroActualizar.setTextFill(Color.GREEN);
                }else{
                    labelULibroActualizar.setVisible(true);
                    labelULibroActualizar.setText("Este libro es nuevo, agrega uno existente.");
                    labelULibroActualizar.setTextFill(Color.YELLOW);
                }
            }catch (SQLException e){
                e.printStackTrace();
                labelULibroActualizar.setVisible(true);
                labelULibroActualizar.setText("Ocurrio un error");
                labelULibroActualizar.setTextFill(Color.RED);
            }


        } else{
            System.out.println(titulo);
            System.out.println(fecha);
            System.out.println(isbn);
            System.out.println(cantidad);
            System.out.println(autor);
            System.out.println(editorial);

            labelULibroActualizar.setVisible(true);
            labelULibroActualizar.setText("LLena todos los campos por favor.");
            labelULibroActualizar.setTextFill(Color.YELLOWGREEN);
        }
    }
    @FXML
    protected void btnCLibroActualizar(){
        am.comboBoxConsultar("titulo", "libro", bxConsultarLibro);
        am.comboBoxConsultar("nombre","autor", cbALibrosAutor);
        am.comboBoxConsultar("nombre", "editorial", cbALibrosEditorial);
        am.comboBoxConsultar("titulo", "libro", bxULibroActualizar);
    }
    @FXML
    protected void btnSeleccionarBorrarLibro(){
        String seleccion = cbBorrarLibro.getValue();
        String query = "SELECT titulo, isbn FROM libro WHERE titulo = '"+seleccion+"'";
        try {
            ResultSet rs = DbConexion.ConsultaSQL(query);
            if (rs.next()){
                BorrarTituloLibro.setText(rs.getString("titulo"));
                BorrarISBNLibro.setText(rs.getString("isbn"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    @FXML
    protected void btnBorrarLibro(){
        boolean check = checkAceptoTerminos.isSelected();
        if (check){
                String titulo = BorrarTituloLibro.getText();
                String isbn = BorrarISBNLibro.getText();
                DbConexion.eliminarRegistro("libro", "titulo", "isbn", titulo, isbn);
                alertBorradoLibro.setVisible(true);
                am.GuardarAccion("una", am.perfilUser(), "eliminación");
                alertBorradoLibro.setText("Libro Eliminado");
                am.comboBoxConsultar("titulo", "libro", cbBorrarLibro);
                BorrarISBNLibro.clear();
                BorrarTituloLibro.clear();
                cbBorrarLibro.getSelectionModel().clearSelection();

        }else {
            alertBorradoLibro.setVisible(true);
            alertBorradoLibro.setText("ACEPTA LOS TÉRMINOS");
        }
    }
    @FXML
    protected void btnActualizarBorrarLibro(){
        am.comboBoxConsultar("titulo", "libro", cbBorrarLibro);
    }
    @FXML
    protected void btnExportarLibro(){
        String titulo = txtConsultarTitulo.getText();
        String isbn = txtConsultarISBN.getText();
        try {
            String query = "SELECT * FROM libro WHERE titulo = '"+titulo+"' AND isbn = '"+isbn+"'";
            ResultSet rs = DbConexion.ConsultaSQL(query);
            JsonArray jsonArray = am.convertResultSetToJson(rs);

            // Obtener ubicación del archivo seleccionado por el usuario
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar archivo JSON");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo JSON", "*.json"));
            File selectedFile = fileChooser.showSaveDialog(null);

            if (selectedFile != null) {
                am.saveJsonToFile(jsonArray, selectedFile.getAbsolutePath());
                am.mostrarMensaje("Datos exportados correctamente a " + selectedFile.getAbsolutePath());
                am.GuardarAccion("una", am.perfilUser(), "exportación");
                am.GuardarAccion("una", am.perfilUser(), "exportación");
            } else {
                am.mostrarMensaje("Exportación cancelada por el usuario.");
            }
        }catch (SQLException e){

        }
    }

    // Autor
    @FXML
    protected void autorActualizar(){
        am.cargarListView(listAutores, "nombre", "autor");
    }
    @FXML
    protected void GuardarAutor(){
        am.GuardarElemento(NombreGuardarAutor, "autor", "nombre", msgGuardado, am.perfilUser());
    }
    @FXML
    protected void GuardarAutorLimpiar(){
        msgGuardado.setVisible(false);
        NombreGuardarAutor.clear();
    }
    @FXML
    protected void autorActualizarUpdate(){
        am.comboBoxConsultar("nombre", "autor", cbAutorActualizar);
    }
    @FXML
    protected void cbAutorActualizar(){
        NombreActualizarAutor.setText(cbAutorActualizar.getValue());
    }
    @FXML
    protected void SaveAutorActualizar(){
        autorActualizarUpdate();
        am.ActualizarElemento("autor", "nombre", NombreActualizarAutor, cbAutorActualizar, msgUpdateAutor, am.perfilUser());
    }
    @FXML
    protected void EliminarAutor(){

        am.EliminarCampo("autor", "nombre", checkEliminarAutor, cbEliminarAutor, "Autor", msgEliminado, am.perfilUser());
    }
    @FXML
    protected void actualizarAutorEliminar(){
        am.comboBoxConsultar("nombre", "autor", cbEliminarAutor);
    }

    // Editorial
    @FXML
    protected void editorialActualizar(){
        am.cargarListView(listEditorial, "nombre", "editorial");
    }
    @FXML
    protected void GuardarEditorial(){
        am.GuardarElemento(NombreGuardarEditorial, "editorial", "nombre", msgGuardadoEditorial, am.perfilUser());
    }
    @FXML
    protected void GuardarEditorialLimpiar(){
        msgGuardadoEditorial.setVisible(false);
        NombreGuardarEditorial.clear();
    }
    @FXML
    protected void editorialActualizarUpdate(){
        am.comboBoxConsultar("nombre", "editorial", cbEditorialActualizar);
    }
    @FXML
    protected void cbEditorialActualizar(){
        NombreActualizarEditorial.setText(cbEditorialActualizar.getValue());
    }
    @FXML
    protected void SaveEditorialActualizar(){
        am.ActualizarElemento("editorial", "nombre", NombreActualizarEditorial, cbEditorialActualizar, msgUpdateEditorial, am.perfilUser());
        autorActualizarUpdate();
    }
    @FXML
    protected void EliminarEditorial(){
        am.EliminarCampo("editorial", "nombre", checkEliminarEditorial, cbEliminarEditorial, "Editorial", msgEliminadoEditorial, am.perfilUser());
    }

    // Perfil
    @FXML
    protected void PerfilActDes() {
        ResultSet rs = am.perfilUser();
        try {
            if (rs.next()) {

                String recopilar = rs.getString("recopilar");
                if (recopilar.equals("true") || recopilar.equals("t")) {
                    PerfilRecoleccion.setSelected(false);
                    PerfilRecoleccion.setText("Recopilación de datos desactivada");
                    PerfilActDes.setText("Activar");
                    String query = "UPDATE usuario SET recopilar = false WHERE id = '" + rs.getString("id") + "'";
                    DbConexion.ejecutarUpdate(query);
                } else {
                    PerfilActDes.setText("Desactivar");
                    PerfilRecoleccion.setSelected(true);
                    PerfilRecoleccion.setText("Recopilación de datos activada");
                    String query = "UPDATE usuario SET recopilar = true WHERE id = '" + rs.getString("id") + "'";
                    DbConexion.ejecutarUpdate(query);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void perfilCargar(){
        ResultSet rs = am.perfilUser();
        try {
            if (rs.next()){
                String code = am.obtenerNombrePorId("codigotelefono", "codigo", "id", Integer.parseInt(rs.getString("codigo_telefono")));
                txtBienvenida.setText("Hola, "+rs.getString("nombre"));
                PerfilName.setText(rs.getString("nombre"));
                PerfilDpi.setText(rs.getString("identificador"));
                PerfilCode.setText(code);
                PerfilTelefono.setText(rs.getString("telefono"));
                PerfilUsername.setText(rs.getString("username"));
                PerfilCorreo.setText(rs.getString("email"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Historial
    @FXML
    protected void ActualizarHistorial(){
        am.cargarListView(historialView, "accion", "historial_acciones");
    }
    @FXML
    protected void ExportarHistorial(){
        utils.ExportarHistorial(historialView);
    }

}
