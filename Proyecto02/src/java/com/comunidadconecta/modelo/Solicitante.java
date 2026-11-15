package com.comunidadconecta.modelo;

/**
 * Subclase que representa al solicitante
 * Autor: Grupo 02 - PRN315 (UES)
 * Fecha: Noviembre 2026
 */
public class Solicitante extends Usuario {

    private String direccion;

    public Solicitante() {
        super();
        setRol("Solicitante");
    }

    public Solicitante(int idUsuario, String nombre, String correo, String contrasena, String telefono, String direccion) {
        super(idUsuario, nombre, correo, contrasena, telefono, "Solicitante");
        this.direccion = direccion;
    }

    @Override
    public String obtenerPermisos() {
        return "Registrar solicitudes de apoyo comunitario y consultar su estado.";
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
