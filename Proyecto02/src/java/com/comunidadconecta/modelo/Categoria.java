package com.comunidadconecta.modelo;

import java.io.Serializable;

/**
 * Clase entidad que clasifica el tipo de apoyo comunitario.
 * Autor: Grupo 02 - PRN315 (UES)
 * Fecha: Noviembre 2026
 */
public class Categoria implements Serializable {

    private int idCategoria;
    private String nombreCategoria;

    public Categoria() {
    }

    public Categoria(int idCategoria, String nombreCategoria) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}
