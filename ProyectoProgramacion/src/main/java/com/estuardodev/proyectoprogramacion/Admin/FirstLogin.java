package com.estuardodev.proyectoprogramacion.Admin;

import com.estuardodev.proyectoprogramacion.Clases.*;
import com.estuardodev.proyectoprogramacion.DataBase.DbConexion;
import com.estuardodev.proyectoprogramacion.ProyectoApplication;
import com.estuardodev.proyectoprogramacion.StageAwareController;
import com.estuardodev.proyectoprogramacion.Usuario;
import com.estuardodev.proyectoprogramacion.Utilidades.Encrypt;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLogin implements StageAwareController {
    private Stage stage;

    @FXML
    TextField nombre, dpi, correo, codigo, telefono, direccion, username, password1, password2;
    @FXML
    CheckBox acepto;
    @FXML
    Button Crear, CrearImportado;
    @FXML
    Label Informacion;
    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void acepto(){
        if (acepto.isSelected()){
            Crear.setDisable(false);
        }else{
            Crear.setDisable(true);
        }
    }

    @FXML
    protected void Crear(){
        String name = nombre.getText();
        String document = dpi.getText();
        String email = correo.getText();
        String code = codigo.getText();
        String number = telefono.getText();
        String address = direccion.getText();
        String user = username.getText();
        String pass1 = password1.getText();
        String pass2 = password2.getText();
        String pass = Encrypt.getSHA256(pass1);
        String id_code;

        if (name.isBlank() || document.isBlank() || email.isBlank() || code.isBlank()
            || number.isBlank() || address.isBlank() || pass1.isBlank() || pass2.isBlank() || user.isBlank()){
            Informacion.setVisible(true);
            Informacion.setText("Rellena todos los campos correctamente :|");
            Informacion.setTextFill(Color.RED);
        }else{
            try {
                String query = "SELECT id FROM codigotelefono WHERE codigo = '"+code+"'";
                ResultSet rs = DbConexion.ConsultaSQL(query);
                if (rs.next()){
                    id_code = rs.getString("id");
                }else{
                    query = "INSERT INTO codigotelefono (codigo) VALUES ('"+code+"')";
                    DbConexion.ejecutarInsercion(query);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }


            String query_get = "SELECT id FROM codigotelefono WHERE codigo = '"+code+"'";
            ResultSet rs = DbConexion.ConsultaSQL(query_get);
            try {
                if (rs.next()){
                    id_code = rs.getString("id");
                    if(!pass1.equals(pass2)){
                        Informacion.setVisible(true);
                        Informacion.setText("Las contraseñas no coinciden :|");
                        Informacion.setTextFill(Color.YELLOWGREEN);
                    }else {

                        Usuario usuario = new Usuario(0, name, document, Integer.parseInt(id_code), number, user, email, pass, address, true);
                        int info = usuario.CrearUsuario();
                        switch (info){
                            case 0:
                                File file = new File("Biblioteca/init.txt");
                                file.createNewFile();
                                FileWriter fw = new FileWriter(file);
                                BufferedWriter bw = new BufferedWriter(fw);
                                bw.write("true");
                                ProyectoApplication pa = new ProyectoApplication();
                                pa.mostrarVista(stage, "Login-view.fxml");
                                break;
                        }
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void ImportarTodo() {
        AdminMetodos adminMetodos = new AdminMetodos();
        boolean admin_exist = false;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir archivo OBJ");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo obj", "*.obj"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Informacion.setVisible(true);
            Informacion.setText("Importando datos...");
            try  {
                Path path = Paths.get(selectedFile.getAbsolutePath());
                InputStream is = Files.newInputStream(path);
                ObjectInputStream ois = new ObjectInputStream(is);
                while (true) {
                    try {
                        Object obj = ois.readObject();
                        if (obj instanceof Libro) {
                            Libro libro = (Libro) obj;
                            libro.CrearLibro();
                        } else if (obj instanceof Autor) {
                            Autor autor = (Autor) obj;
                            autor.CrearAutor();
                        } else if (obj instanceof Editorial) {
                            Editorial editorial = (Editorial) obj;
                            editorial.CrearEditorial();
                        } else if (obj instanceof Usuario) {
                            Usuario usuario = (Usuario) obj;
                            if (usuario.getAdmin()){
                                admin_exist = true;
                                System.out.println("Existe administrador");
                            }
                            usuario.CrearUsuario();
                        } else if (obj instanceof CodigoTelefono) {
                            CodigoTelefono codigoTelefono = (CodigoTelefono) obj;
                            codigoTelefono.CrearCodigoTelefono();
                        } else if (obj instanceof HistorialAcciones) {
                            HistorialAcciones historialAcciones = (HistorialAcciones) obj;
                            historialAcciones.CrearHistorialAcciones();
                        } else {
                            break;
                        }
                    } catch (EOFException e) {
                        break;
                    }
                }

                if (admin_exist){
                    CrearImportado.setVisible(true);
                    CrearImportado.setDisable(false);
                    CrearImportado.setText("Continuar...");
                    Crear.setVisible(false);
                }

                Informacion.setVisible(true);
                Informacion.setText("Datos Importados");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            adminMetodos.mostrarMensaje("Importación cancelada por el usuario.");
        }
    }


    @FXML
    protected void CrearImportado(){
        try {
            ProyectoApplication pa = new ProyectoApplication();
            pa.mostrarVista(stage, "Login-view.fxml");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
