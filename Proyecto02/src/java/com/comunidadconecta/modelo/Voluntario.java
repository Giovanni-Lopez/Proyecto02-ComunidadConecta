package com.comunidadconecta.modelo;

/**
 * Subclase que representa al voluntario encargado de atender solicitudes.
* Autores: 
 * Alexis Giovanni Angel Lopez - AL23036 
 * Juan Diego Chicas - CC23118
 * Grupo 02 - PRN315 (UES)
 * Fecha: Noviembre 2026
 */
public class Voluntario extends Usuario {

    private String disponibilidad;

    public Voluntario() {
        super();
        setRol("Voluntario");
    }

    public Voluntario(int idUsuario, String nombre, String correo, String contrasena, String telefono, String disponibilidad) {
        super(idUsuario, nombre, correo, contrasena, telefono, "Voluntario");
        this.disponibilidad = disponibilidad;
    }

    @Override
    public String obtenerPermisos() {
        return "Atender solicitudes asignadas, actualizar estados y registrar seguimientos.";
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
}
