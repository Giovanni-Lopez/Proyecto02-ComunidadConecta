package com.comunidadconecta.controlador;

import com.comunidadconecta.dao.UsuarioDAO;
import com.comunidadconecta.modelo.Administrador;
import com.comunidadconecta.modelo.Solicitante;
import com.comunidadconecta.modelo.Usuario;
import com.comunidadconecta.modelo.Voluntario;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * Autor: Alexis Giovanni Angel Lopez - AL23036 
 * Juan Diego Chicas - CC23118
 * Grupo 02 - PRN315 (UES)
 * Fecha: Noviembre 2026
 */

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private String correo;
    private String contrasena;
    private Usuario usuarioAutenticado;

    // Campos para registro de nuevo usuario
    private String regNombre;
    private String regCorreo;
    private String regContrasena;
    private String regTelefono;
    private String regRol = "Solicitante";
    private String regExtra; 

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Procesa el inicio de sesión, validando campos vacíos y formato de correo.
     */
    public String autenticar() {
        // Validación para campos vacíos
        if (correo == null || correo.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
            agregarMensaje(FacesMessage.SEVERITY_WARN, "Campos requeridos", "Debe ingresar su correo y contraseña.");
            return null;
        }
        // Validación para formato básico de correo
        if (!correo.contains("@") || !correo.contains(".")) {
            agregarMensaje(FacesMessage.SEVERITY_WARN, "Formato inválido", "Ingrese un correo electrónico válido.");
            return null;
        }

        try {
            usuarioAutenticado = usuarioDAO.iniciarSesion(correo, contrasena);
            if (usuarioAutenticado != null) {
                return "solicitudes?faces-redirect=true";
            } else {
                agregarMensaje(FacesMessage.SEVERITY_ERROR, "Acceso denegado", "Correo o contraseña incorrectos.");
                return null;
            }
        } catch (Exception e) {
            agregarMensaje(FacesMessage.SEVERITY_ERROR, "Error de conexión", "Verifique SQL Server: " + e.getMessage());
            return null;
        }
    }

    /**
     * Este metodo egistra un nuevo usuario en el sistema validando todas las entradas.
     */
    public void registrarNuevoUsuario() {
        if (regNombre == null || regNombre.trim().isEmpty()
                || regCorreo == null || regCorreo.trim().isEmpty()
                || regContrasena == null || regContrasena.trim().isEmpty()
                || regTelefono == null || regTelefono.trim().isEmpty()) {
            agregarMensaje(FacesMessage.SEVERITY_WARN, "Validación", "Todos los campos obligatorios deben completarse.");
            return;
        }
        if (!regCorreo.contains("@") || !regCorreo.contains(".")) {
            agregarMensaje(FacesMessage.SEVERITY_WARN, "Validación", "El formato del correo electrónico no es válido.");
            return;
        }
        if (!regTelefono.matches("[0-9-]{8,15}")) {
            agregarMensaje(FacesMessage.SEVERITY_WARN, "Validación", "El teléfono debe contener solo números y guiones (ej. 7000-0000).");
            return;
        }

        try {
            Usuario nuevo;
            if ("Voluntario".equalsIgnoreCase(regRol)) {
                Voluntario v = new Voluntario();
                v.setDisponibilidad(regExtra != null && !regExtra.trim().isEmpty() ? regExtra.trim() : "Por definir");
                nuevo = v;
            } else if ("Administrador".equalsIgnoreCase(regRol)) {
                nuevo = new Administrador();
            } else {
                Solicitante s = new Solicitante();
                s.setDireccion(regExtra != null && !regExtra.trim().isEmpty() ? regExtra.trim() : "Sin dirección especificada");
                nuevo = s;
            }
            nuevo.setNombre(regNombre.trim());
            nuevo.setCorreo(regCorreo.trim());
            nuevo.setContrasena(regContrasena.trim());
            nuevo.setTelefono(regTelefono.trim());

            if (usuarioDAO.registrarUsuario(nuevo)) {
                agregarMensaje(FacesMessage.SEVERITY_INFO, "Registro exitoso", "Usuario creado. Ya puede iniciar sesión.");
                regNombre = "";
                regCorreo = "";
                regContrasena = "";
                regTelefono = "";
                regExtra = "";
            }
        } catch (Exception e) {
            agregarMensaje(FacesMessage.SEVERITY_ERROR, "Error al registrar", e.getMessage());
        }
    }

    public String cerrarSesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index?faces-redirect=true";
    }

    private void agregarMensaje(FacesMessage.Severity severidad, String resumen, String detalle) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidad, resumen, detalle));
    }

    // Getters y Setters
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public Usuario getUsuarioAutenticado() { return usuarioAutenticado; }
    public String getRegNombre() { return regNombre; }
    public void setRegNombre(String regNombre) { this.regNombre = regNombre; }
    public String getRegCorreo() { return regCorreo; }
    public void setRegCorreo(String regCorreo) { this.regCorreo = regCorreo; }
    public String getRegContrasena() { return regContrasena; }
    public void setRegContrasena(String regContrasena) { this.regContrasena = regContrasena; }
    public String getRegTelefono() { return regTelefono; }
    public void setRegTelefono(String regTelefono) { this.regTelefono = regTelefono; }
    public String getRegRol() { return regRol; }
    public void setRegRol(String regRol) { this.regRol = regRol; }
    public String getRegExtra() { return regExtra; }
    public void setRegExtra(String regExtra) { this.regExtra = regExtra; }
}
