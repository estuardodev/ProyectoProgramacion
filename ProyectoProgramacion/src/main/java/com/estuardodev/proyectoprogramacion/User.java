package com.estuardodev.proyectoprogramacion;

public abstract class User {

    protected String id, nombre, dpi, codigo, telefono, username, correo, password, address;
    protected boolean admin;

    public void setDatos(String _id, String _nombre, String _dpi, String _codigo, String _telefono,
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

    public abstract int CrearUsuario();

    public abstract int ActualizarUsuario();
}

