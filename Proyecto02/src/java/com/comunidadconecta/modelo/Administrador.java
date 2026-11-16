package com.comunidadconecta.modelo;

/**
 * Subclase que representa al administrador general de la plataforma.
 * Autores: 
 * Alexis Giovanni Angel Lopez - AL23036 
 * Juan Diego Chicas - CC23118
 * Grupo 02 - PRN315 (UES)
 * Fecha: Noviembre 2026
 */
public class Administrador extends Usuario {

    public Administrador() {
        super();
        setRol("Administrador");
    }

    public Administrador(int idUsuario, String nombre, String correo, String contrasena, String telefono) {
        super(idUsuario, nombre, correo, contrasena, telefono, "Administrador");
    }

    @Override
    public String obtenerPermisos() {
        return "Control total de usuarios, asignación de solicitudes, estadísticas y reportes.";
    }
}
