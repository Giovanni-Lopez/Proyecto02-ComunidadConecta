package com.comunidadconecta.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Autores: 
 * Alexis Giovanni Angel Lopez - AL23036 
 * Juan Diego Chicas - CC23118
 * Grupo 02 - PRN315 (UES)
 * Fecha: Noviembre 2026
 */

// Metodo para la conexion a la base de datos 
public class Conexion {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=BD_ComunidadConecta;encrypt=false;";
    private static final String USER = "sa";
    private static final String PASS = "123";

    //Abre y devuelve una conexión activa a la base de datos.     
    public static Connection conectar() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
