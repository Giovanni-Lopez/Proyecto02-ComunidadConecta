package com.comunidadconecta.dao;

import com.comunidadconecta.modelo.Categoria;
import com.comunidadconecta.modelo.Organizacion;
import com.comunidadconecta.modelo.Seguimiento;
import com.comunidadconecta.modelo.Solicitud;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Autor: al23036 Grupo 02 - PRN315 (UES)
 * Fecha: Noviembre 2026
 */

public class SolicitudDAO {

    /**
     * Insertamos una nueva solicitud de apoyo comunitario.
     * Entrada: Objeto Solicitud validado.
     * Proceso: Ejecuta INSERT en tabla Solicitud con estado inicial 'Pendiente'.
     * Salida: true si se insertó correctamente.
     */ 
   public boolean registrarSolicitud(Solicitud sol) throws Exception {
        String sql = "INSERT INTO Solicitud (descripcion, fechaRegistro, estado, idSolicitante, idVoluntario, idCategoria, idOrganizacion) " +
                     "VALUES (?, GETDATE(), 'Pendiente', ?, ?, ?, ?)";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, sol.getDescripcion().trim());
            ps.setInt(2, sol.getIdSolicitante());

            if (sol.getIdVoluntario() != null && sol.getIdVoluntario() > 0) {
                ps.setInt(3, sol.getIdVoluntario());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setInt(4, sol.getIdCategoria());

            if (sol.getIdOrganizacion() != null && sol.getIdOrganizacion() > 0) {
                ps.setInt(5, sol.getIdOrganizacion());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Consulta todas las solicitudes uniendo nombres de solicitante, voluntario, categoría y organización.
     */
    public List<Solicitud> listarSolicitudes() throws Exception {
        List<Solicitud> lista = new ArrayList<>();
        String sql = "SELECT s.idSolicitud, s.descripcion, s.fechaRegistro, s.estado, " +
                     "s.idSolicitante, uSol.nombre AS nombreSolicitante, " +
                     "s.idVoluntario, ISNULL(uVol.nombre, 'Sin asignar') AS nombreVoluntario, " +
                     "s.idCategoria, c.nombreCategoria, " +
                     "s.idOrganizacion, ISNULL(o.nombre, 'Pendiente de enlace') AS nombreOrganizacion " +
                     "FROM Solicitud s " +
                     "INNER JOIN Usuario uSol ON s.idSolicitante = uSol.idUsuario " +
                     "LEFT JOIN Usuario uVol ON s.idVoluntario = uVol.idUsuario " +
                     "INNER JOIN Categoria c ON s.idCategoria = c.idCategoria " +
                     "LEFT JOIN Organizacion o ON s.idOrganizacion = o.idOrganizacion " +
                     "ORDER BY s.idSolicitud DESC";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Solicitud s = new Solicitud();
                s.setIdSolicitud(rs.getInt("idSolicitud"));
                s.setDescripcion(rs.getString("descripcion"));
                s.setFechaRegistro(rs.getDate("fechaRegistro"));
                s.setEstado(rs.getString("estado"));
                s.setIdSolicitante(rs.getInt("idSolicitante"));
                s.setNombreSolicitante(rs.getString("nombreSolicitante"));
                s.setIdVoluntario(rs.getObject("idVoluntario") != null ? rs.getInt("idVoluntario") : null);
                s.setNombreVoluntario(rs.getString("nombreVoluntario"));
                s.setIdCategoria(rs.getInt("idCategoria"));
                s.setNombreCategoria(rs.getString("nombreCategoria"));
                s.setIdOrganizacion(rs.getObject("idOrganizacion") != null ? rs.getInt("idOrganizacion") : null);
                s.setNombreOrganizacion(rs.getString("nombreOrganizacion"));
                lista.add(s);
            }
        }
        return lista;
    }

    /**
     * Actualiza el estado de una solicitud (Pendiente, En proceso, Atendida, Cerrada).
     */
    public boolean cambiarEstado(int idSolicitud, String nuevoEstado) throws Exception {
        String sql = "UPDATE Solicitud SET estado = ? WHERE idSolicitud = ?";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idSolicitud);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Asigna un voluntario responsable y cambia el estado a 'En proceso'.
     */
    public boolean asignarVoluntario(int idSolicitud, int idVoluntario) throws Exception {
        String sql = "UPDATE Solicitud SET idVoluntario = ?, estado = 'En proceso' WHERE idSolicitud = ?";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idVoluntario);
            ps.setInt(2, idSolicitud);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Registra un evento de seguimiento en la bitácora de una solicitud.
     */
    public boolean registrarSeguimiento(Seguimiento seg) throws Exception {
        String sql = "INSERT INTO Seguimiento (fecha, comentario, idSolicitud) VALUES (GETDATE(), ?, ?)";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, seg.getComentario().trim());
            ps.setInt(2, seg.getIdSolicitud());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Este metodo muestra el historial de seguimientos de una solicitud específica.
     */
    public List<Seguimiento> listarSeguimientosPorSolicitud(int idSolicitud) throws Exception {
        List<Seguimiento> lista = new ArrayList<>();
        String sql = "SELECT idSeguimiento, fecha, comentario, idSolicitud " +
                     "FROM Seguimiento WHERE idSolicitud = ? ORDER BY idSeguimiento DESC";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idSolicitud);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Seguimiento seg = new Seguimiento();
                    seg.setIdSeguimiento(rs.getInt("idSeguimiento"));
                    seg.setFecha(rs.getDate("fecha"));
                    seg.setComentario(rs.getString("comentario"));
                    seg.setIdSolicitud(rs.getInt("idSolicitud"));
                    lista.add(seg);
                }
            }
        }
        return lista;
    }

    /**
     * Metodo para cargar el catálogo de categorías desde SQL Server.
     */
    public List<Categoria> listarCategorias() throws Exception {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT idCategoria, nombreCategoria FROM Categoria ORDER BY nombreCategoria ASC";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Categoria(rs.getInt("idCategoria"), rs.getString("nombreCategoria")));
            }
        }
        return lista;
    }

    /**
     * Metodo para la carga de el catálogo de organizaciones colaboradoras desde SQL Server.
     */
    public List<Organizacion> listarOrganizaciones() throws Exception {
        List<Organizacion> lista = new ArrayList<>();
        String sql = "SELECT idOrganizacion, nombre, tipoApoyo FROM Organizacion ORDER BY nombre ASC";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Organizacion(rs.getInt("idOrganizacion"), rs.getString("nombre"), rs.getString("tipoApoyo")));
            }
        }
        return lista;
    }
    //Metodo para que administrador pueda eliminar solicitudes
    public boolean eliminarSolicitud(int idSolicitud) throws Exception {
    String sqlSeg = "DELETE FROM Seguimiento WHERE idSolicitud = ?";
    String sqlSol = "DELETE FROM Solicitud WHERE idSolicitud = ?";
    try (Connection cn = Conexion.conectar()) {
        try (PreparedStatement ps1 = cn.prepareStatement(sqlSeg)) {
            ps1.setInt(1, idSolicitud);
            ps1.executeUpdate();
        }
        try (PreparedStatement ps2 = cn.prepareStatement(sqlSol)) {
            ps2.setInt(1, idSolicitud);
            return ps2.executeUpdate() > 0;
            }
        }
    }
    
    
    
     /**
     * Actualiza los datos principales de una solicitud
     */
    public boolean actualizarSolicitud(Solicitud sol) throws Exception {
        String sql = "UPDATE Solicitud SET descripcion = ?, estado = ?, idCategoria = ?, " +
                     "idOrganizacion = ?, idVoluntario = ? WHERE idSolicitud = ?";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, sol.getDescripcion().trim());
            ps.setString(2, sol.getEstado());
            ps.setInt(3, sol.getIdCategoria());

            if (sol.getIdOrganizacion() != null && sol.getIdOrganizacion() > 0) {
                ps.setInt(4, sol.getIdOrganizacion());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            if (sol.getIdVoluntario() != null && sol.getIdVoluntario() > 0) {
                ps.setInt(5, sol.getIdVoluntario());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            ps.setInt(6, sol.getIdSolicitud());
            return ps.executeUpdate() > 0;
        }
    }

}
