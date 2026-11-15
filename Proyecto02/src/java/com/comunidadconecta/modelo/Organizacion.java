package com.comunidadconecta.modelo;

import java.io.Serializable;

/**
 * Clase entidad que representa a las organizaciones aliadas que brindan recursos.
 * Autor: Grupo 02 - PRN315 (UES)
 * Fecha: Noviembre 2026
 */
public class Organizacion implements Serializable {

    private int idOrganizacion;
    private String nombre;
    private String tipoApoyo;

    public Organizacion() {
    }

    public Organizacion(int idOrganizacion, String nombre, String tipoApoyo) {
        this.idOrganizacion = idOrganizacion;
        this.nombre = nombre;
        this.tipoApoyo = tipoApoyo;
    }

    public int getIdOrganizacion() {
        return idOrganizacion;
    }

    public void setIdOrganizacion(int idOrganizacion) {
        this.idOrganizacion = idOrganizacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoApoyo() {
        return tipoApoyo;
    }

    public void setTipoApoyo(String tipoApoyo) {
        this.tipoApoyo = tipoApoyo;
    }
}
