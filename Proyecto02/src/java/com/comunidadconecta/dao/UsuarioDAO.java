package com.comunidadconecta.dao;

import com.comunidadconecta.modelo.Administrador;
import com.comunidadconecta.modelo.Solicitante;
import com.comunidadconecta.modelo.Usuario;
import com.comunidadconecta.modelo.Voluntario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para autenticación, registrAutor: al23036 Grupo 02 - PRN315 (UES)
 * Autor: Grupo 02 - PRN315 (UES)
 * Fecha: Noviembre 2026
 */
public class UsuarioDAO {

    /**
     * Valida credenciales e instancia dinámicamente la subclase correspondiente (Polimorfismo).
     * Entrada: correo y contraseña.
     * Proceso: Consulta SQL parametrizada en tabla Usuario.
     * Salida: Instancia de Solicitante, Voluntario o Administrador.
     */
    public Usuario iniciarSesion(String correo, String contrasena) throws Exception {
        String sql = "SELECT idUsuario, nombre, correo, contrasena, telefono, rol, direccion, disponibilidad " +
                     "FROM Usuario WHERE correo = ? AND contrasena = ?";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo.trim());
            ps.setString(2, contrasena.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String rol = rs.getString("rol");
                    Usuario user;
                    if ("Administrador".equalsIgnoreCase(rol)) {
                        user = new Administrador();
                    } else if ("Voluntario".equalsIgnoreCase(rol)) {
                        Voluntario vol = new Voluntario();
                        vol.setDisponibilidad(rs.getString("disponibilidad"));
                        user = vol;
                    } else {
                        Solicitante sol = new Solicitante();
                        sol.setDireccion(rs.getString("direccion"));
                        user = sol;
                    }
                    user.setIdUsuario(rs.getInt("idUsuario"));
                    user.setNombre(rs.getString("nombre"));
                    user.setCorreo(rs.getString("correo"));
                    user.setContrasena(rs.getString("contrasena"));
                    user.setTelefono(rs.getString("telefono"));
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     */
    public boolean registrarUsuario(Usuario u) throws Exception {
        String sql = "INSERT INTO Usuario (nombre, correo, contrasena, telefono, rol, direccion, disponibilidad) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getContrasena());
            ps.setString(4, u.getTelefono());
            ps.setString(5, u.getRol());
            if (u instanceof Solicitante) {
                ps.setString(6, ((Solicitante) u).getDireccion());
                ps.setNull(7, java.sql.Types.VARCHAR);
            } else if (u instanceof Voluntario) {
                ps.setNull(6, java.sql.Types.VARCHAR);
                ps.setString(7, ((Voluntario) u).getDisponibilidad());
            } else {
                ps.setNull(6, java.sql.Types.VARCHAR);
                ps.setNull(7, java.sql.Types.VARCHAR);
            }
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Con este metodo podemos ver la lista de eusuarios filtrados por rol  'Solicitante' o 'Voluntario')
     */
    public List<Usuario> listarPorRol(String rolBuscado) throws Exception {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT idUsuario, nombre, correo, telefono, rol FROM Usuario WHERE rol = ? ORDER BY nombre ASC";
        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, rolBuscado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario u = "Voluntario".equalsIgnoreCase(rolBuscado) ? new Voluntario() : new Solicitante();
                    u.setIdUsuario(rs.getInt("idUsuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setCorreo(rs.getString("correo"));
                    u.setTelefono(rs.getString("telefono"));
                    lista.add(u);
                }
            }
        }
        return lista;
    }
}
