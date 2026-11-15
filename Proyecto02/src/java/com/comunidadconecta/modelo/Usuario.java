package com.comunidadconecta.modelo;

import java.io.Serializable;

/**
 * Clase abstracta base que modela a los usuarios del sistema según el diagrama UML.
 * Autor: Grupo 02 - PRN315 (UES)
 * Fecha: Noviembre 2026
 */
public abstract class Usuario implements Serializable {

    private int idUsuario;
    private String nombre;
    private String correo;
    private String contrasena;
    private String telefono;
    private String rol;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombre, String correo, String contrasena, String telefono, String rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.rol = rol;
    }

    /**
     * Método polimórfico implementado por cada tipo de usuario.
     * @return Descripción de privilegios del rol.
     */
    public abstract String obtenerPermisos();

    // Getters y Setters 
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
