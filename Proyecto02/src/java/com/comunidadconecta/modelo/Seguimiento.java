package com.comunidadconecta.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase entidad que registra las acciones de bitácora sobre una solicitud.
 * Autores: 
 * Alexis Giovanni Angel Lopez - AL23036 
 * Juan Diego Chicas - CC23118
 * Grupo 02 - PRN315 (UES)
 * Fecha: Noviembre 2026
 */
public class Seguimiento implements Serializable {

    private int idSeguimiento;
    private Date fecha;
    private String comentario;
    private int idSolicitud;

    public Seguimiento() {
        this.fecha = new Date();
    }

    public int getIdSeguimiento() {
        return idSeguimiento;
    }

    public void setIdSeguimiento(int idSeguimiento) {
        this.idSeguimiento = idSeguimiento;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }
}
