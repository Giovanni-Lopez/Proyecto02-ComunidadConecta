package com.comunidadconecta.controlador;

import com.comunidadconecta.dao.SolicitudDAO;
import com.comunidadconecta.dao.UsuarioDAO;
import com.comunidadconecta.modelo.Categoria;
import com.comunidadconecta.modelo.Organizacion;
import com.comunidadconecta.modelo.Seguimiento;
import com.comunidadconecta.modelo.Solicitud;
import com.comunidadconecta.modelo.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

/**
 * Autores: 
 * Alexis Giovanni Angel Lopez - AL23036 
 * Juan Diego Chicas - CC23118
 * Grupo 02 - PRN315 (UES)
 * Fecha: Noviembre 2026
 */

@ManagedBean(name = "solicitudBean")
@ViewScoped
public class SolicitudBean implements Serializable {

    private Solicitud nuevaSolicitud = new Solicitud();
    private Solicitud solicitudSeleccionada = new Solicitud();
    private Seguimiento nuevoSeguimiento = new Seguimiento();
    private int idVoluntarioAsignar;

    private List<Solicitud> listaSolicitudes = new ArrayList<>();
    private List<Seguimiento> historialSeguimiento = new ArrayList<>();
    private List<Categoria> listaCategorias = new ArrayList<>();
    private List<Organizacion> listaOrganizaciones = new ArrayList<>();
    private List<Usuario> listaSolicitantes = new ArrayList<>();
    private List<Usuario> listaVoluntarios = new ArrayList<>();

    private final SolicitudDAO solicitudDAO = new SolicitudDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @PostConstruct
    public void init() {
        cargarTodo();
    }

    /**
     * Metodo para cargar las listas de solicitudes y catálogos desde SQL Server.
     */
    public void cargarTodo() {
        try {
            listaSolicitudes = solicitudDAO.listarSolicitudes();
            listaCategorias = solicitudDAO.listarCategorias();
            listaOrganizaciones = solicitudDAO.listarOrganizaciones();
            listaSolicitantes = usuarioDAO.listarPorRol("Solicitante");
            listaVoluntarios = usuarioDAO.listarPorRol("Voluntario");
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error de Base de Datos", e.getMessage());
        }
    }

    /**
     * Este metodo valida y guarda una nueva solicitud de apoyo comunitario.
     */
    public void guardarSolicitud() {
        // Valida la selección positiva (evita 0 o negativos)
        if (nuevaSolicitud.getIdSolicitante() <= 0) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Validación", "Debe seleccionar un solicitante válido (ID > 0).");
            return;
        }
        if (nuevaSolicitud.getIdCategoria() <= 0) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Validación", "Debe seleccionar una categoría de apoyo.");
            return;
        }
        // Valida la entrada vacía y longitud mínima
        if (nuevaSolicitud.getDescripcion() == null || nuevaSolicitud.getDescripcion().trim().isEmpty()) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Validación", "La descripción de la solicitud no puede estar vacía.");
            return;
        }
        if (nuevaSolicitud.getDescripcion().trim().length() < 10) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Validación", "Ingrese una descripción más detallada (mínimo 10 caracteres).");
            return;
        }

        try {
            if (solicitudDAO.registrarSolicitud(nuevaSolicitud)) {
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Solicitud Registrada", "La necesidad comunitaria se guardó en SQL Server.");
                nuevaSolicitud = new Solicitud();
                cargarTodo();
            }
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al guardar", e.getMessage());
        }
    }

    /**
     * Este metodo sirve para cambiar el estado de una solicitud en el flujo comunitario.
     */
    public void cambiarEstado(Solicitud sol, String nuevoEstado) {
        try {
            if (solicitudDAO.cambiarEstado(sol.getIdSolicitud(), nuevoEstado)) {
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Estado Actualizado",
                        "Solicitud #" + sol.getIdSolicitud() + " pasó a estado: " + nuevoEstado);
                cargarTodo();
            }
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
        }
    }

    /**
     * Este metodo sirve para seleccionar una solicitud para ver su historial de seguimiento o asignar voluntario.
     */
    public void prepararSeguimiento(Solicitud sol) {
        this.solicitudSeleccionada = sol;
        this.nuevoSeguimiento = new Seguimiento();
        this.idVoluntarioAsignar = sol.getIdVoluntario() != null ? sol.getIdVoluntario() : 0;
        try {
            this.historialSeguimiento = solicitudDAO.listarSeguimientosPorSolicitud(sol.getIdSolicitud());
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al cargar historial", e.getMessage());
        }
    }

    /**
     * Con este metodo asignamos a un voluntario a la solicitud seleccionada validando ID positivo.
     */
    public void asignarVoluntario() {
        if (solicitudSeleccionada == null || solicitudSeleccionada.getIdSolicitud() <= 0) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Validación", "Seleccione primero una solicitud.");
            return;
        }
        if (idVoluntarioAsignar <= 0) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Validación", "Seleccione un voluntario válido.");
            return;
        }
        try {
            if (solicitudDAO.asignarVoluntario(solicitudSeleccionada.getIdSolicitud(), idVoluntarioAsignar)) {
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Voluntario Asignado", "La solicitud pasó a 'En proceso'.");
                cargarTodo();
            }
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
        }
    }

    /**
     * Con este metodo registramos un comentario de bitácora en la tabla Seguimiento.
     */
    public void guardarSeguimiento() {
        if (solicitudSeleccionada == null || solicitudSeleccionada.getIdSolicitud() <= 0) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Validación", "No hay una solicitud seleccionada.");
            return;
        }
        if (nuevoSeguimiento.getComentario() == null || nuevoSeguimiento.getComentario().trim().isEmpty()) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Validación", "El comentario de seguimiento no puede estar vacío.");
            return;
        }
        try {
            nuevoSeguimiento.setIdSolicitud(solicitudSeleccionada.getIdSolicitud());
            if (solicitudDAO.registrarSeguimiento(nuevoSeguimiento)) {
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Seguimiento Guardado", "Se añadió el avance a la bitácora.");
                nuevoSeguimiento = new Seguimiento();
                historialSeguimiento = solicitudDAO.listarSeguimientosPorSolicitud(solicitudSeleccionada.getIdSolicitud());
            }
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
        }
    }
    
    //Metodo para que administrador pueda eliminar solicitud
    public void eliminarSolicitud(Solicitud sol) {
    try {
        if (solicitudDAO.eliminarSolicitud(sol.getIdSolicitud())) {
            mostrarMensaje(FacesMessage.SEVERITY_INFO, "Eliminada",
                    "Solicitud #" + sol.getIdSolicitud() + " eliminada correctamente.");
            cargarTodo();
        }
    } catch (Exception e) {
        mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al eliminar", e.getMessage());
        }
    }
    
    
     /**
     * Carga una copia de la solicitud seleccionada para editarla en el modal sin alterar
     */
    public void prepararEdicion(Solicitud sol) {
        Solicitud copia = new Solicitud();
        copia.setIdSolicitud(sol.getIdSolicitud());
        copia.setDescripcion(sol.getDescripcion());
        copia.setEstado(sol.getEstado());
        copia.setIdSolicitante(sol.getIdSolicitante());
        copia.setNombreSolicitante(sol.getNombreSolicitante());
        copia.setIdCategoria(sol.getIdCategoria());
        copia.setIdOrganizacion(sol.getIdOrganizacion() != null ? sol.getIdOrganizacion() : 0);
        copia.setIdVoluntario(sol.getIdVoluntario() != null ? sol.getIdVoluntario() : 0);
        this.solicitudSeleccionada = copia;
    }

    /**
     * Valida los datos modificados y ejecuta el UPDATE en SQL Server.
     */
    public void actualizarSolicitud() {
        if (solicitudSeleccionada == null || solicitudSeleccionada.getIdSolicitud() <= 0) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Validación", "No hay una solicitud válida seleccionada.");
            return;
        }
        if (solicitudSeleccionada.getIdCategoria() <= 0) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Validación", "Debe seleccionar una categoría válida.");
            return;
        }
        if (solicitudSeleccionada.getDescripcion() == null || solicitudSeleccionada.getDescripcion().trim().isEmpty()) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Validación", "La descripción no puede quedar vacía.");
            return;
        }
        if (solicitudSeleccionada.getDescripcion().trim().length() < 10) {
            mostrarMensaje(FacesMessage.SEVERITY_WARN, "Validación", "La descripción debe tener al menos 10 caracteres.");
            return;
        }

        try {
            if (solicitudDAO.actualizarSolicitud(solicitudSeleccionada)) {
                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Actualizada",
                        "Solicitud #" + solicitudSeleccionada.getIdSolicitud() + " modificada correctamente.");
                cargarTodo();
                PrimeFaces.current().executeScript("PF('dlgEditar').hide();");
            }
        } catch (Exception e) {
            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al actualizar", e.getMessage());
        }
    }

    // Métodos para Panel de Estadísticas
    public long getTotalPendientes() {
        return listaSolicitudes.stream().filter(s -> "Pendiente".equalsIgnoreCase(s.getEstado())).count();
    }

    public long getTotalEnProceso() {
        return listaSolicitudes.stream().filter(s -> "En proceso".equalsIgnoreCase(s.getEstado())).count();
    }

    public long getTotalAtendidas() {
        return listaSolicitudes.stream().filter(s -> "Atendida".equalsIgnoreCase(s.getEstado()) || "Cerrada".equalsIgnoreCase(s.getEstado())).count();
    }

    private void mostrarMensaje(FacesMessage.Severity severidad, String resumen, String detalle) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidad, resumen, detalle));
    }

    // Getters y Setters
    public Solicitud getNuevaSolicitud() { return nuevaSolicitud; }
    public void setNuevaSolicitud(Solicitud nuevaSolicitud) { this.nuevaSolicitud = nuevaSolicitud; }
    public Solicitud getSolicitudSeleccionada() { return solicitudSeleccionada; }
    public void setSolicitudSeleccionada(Solicitud solicitudSeleccionada) { this.solicitudSeleccionada = solicitudSeleccionada; }
    public Seguimiento getNuevoSeguimiento() { return nuevoSeguimiento; }
    public void setNuevoSeguimiento(Seguimiento nuevoSeguimiento) { this.nuevoSeguimiento = nuevoSeguimiento; }
    public int getIdVoluntarioAsignar() { return idVoluntarioAsignar; }
    public void setIdVoluntarioAsignar(int idVoluntarioAsignar) { this.idVoluntarioAsignar = idVoluntarioAsignar; }
    public List<Solicitud> getListaSolicitudes() { return listaSolicitudes; }
    public List<Seguimiento> getHistorialSeguimiento() { return historialSeguimiento; }
    public List<Categoria> getListaCategorias() { return listaCategorias; }
    public List<Organizacion> getListaOrganizaciones() { return listaOrganizaciones; }
    public List<Usuario> getListaSolicitantes() { return listaSolicitantes; }
    public List<Usuario> getListaVoluntarios() { return listaVoluntarios; }
}
