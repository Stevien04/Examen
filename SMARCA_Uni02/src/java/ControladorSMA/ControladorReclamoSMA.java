package ControladorSMA;

import ModeloDAOSMA.ClsReclamoDAOSMA;
import ModeloSMA.ClsCategoriaSMA;
import ModeloSMA.ClsReclamoSMA;
import ModeloSMA.ClsResumenSMA;
import ModeloSMA.ClsSeguimientoSMA;
import ModeloSMA.ClsUsuarioSMA;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ControladorReclamoSMA", urlPatterns = {"/ControladorReclamoSMA"})
public class ControladorReclamoSMA extends HttpServlet {

    private final ClsReclamoDAOSMA reclamoDAO;

    public ControladorReclamoSMA() {
        this.reclamoDAO = new ClsReclamoDAOSMA();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioActual") == null) {
            response.sendRedirect(request.getContextPath() + "/ControladorLoginSMA");
            return;
        }

        ClsUsuarioSMA usuario = (ClsUsuarioSMA) session.getAttribute("usuarioActual");
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = usuario.esAdministrador() ? "panelAdmin" : "panelUsuario";
        }

        switch (accion) {
            case "panelUsuario":
                cargarPanelUsuario(request, response, usuario);
                break;
            case "nuevo":
                mostrarFormularioReclamo(request, response, usuario);
                break;
            case "listar":
                listarReclamosUsuario(request, response, usuario);
                break;
            case "historial":
                mostrarHistorial(request, response, usuario);
                break;
            case "panelAdmin":
                if (!usuario.esAdministrador()) {
                    response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA?accion=panelUsuario");
                    return;
                }
                cargarPanelAdmin(request, response);
                break;
            case "gestion":
                if (!usuario.esAdministrador()) {
                    response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA?accion=panelUsuario");
                    return;
                }
                listarReclamosAdmin(request, response);
                break;
            case "detalle":
                if (!usuario.esAdministrador()) {
                    response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA?accion=listar");
                    return;
                }
                mostrarDetalleAdmin(request, response);
                break;
            case "reportes":
                if (!usuario.esAdministrador()) {
                    response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA?accion=panelUsuario");
                    return;
                }
                mostrarReportes(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioActual") == null) {
            response.sendRedirect(request.getContextPath() + "/ControladorLoginSMA");
            return;
        }
        ClsUsuarioSMA usuario = (ClsUsuarioSMA) session.getAttribute("usuarioActual");

        String accion = request.getParameter("accion");
        if ("registrar".equalsIgnoreCase(accion)) {
            registrarReclamo(request, response, usuario);
        } else if ("actualizarEstado".equalsIgnoreCase(accion) && usuario.esAdministrador()) {
            actualizarEstado(request, response, usuario);
        } else if ("agregarSeguimiento".equalsIgnoreCase(accion) && usuario.esAdministrador()) {
            registrarSeguimientoManual(request, response, usuario);
        } else {
            response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA");
        }
    }

    private void cargarPanelUsuario(HttpServletRequest request, HttpServletResponse response, ClsUsuarioSMA usuario) throws ServletException, IOException {
        List<ClsReclamoSMA> reclamos = reclamoDAO.listarReclamosPorUsuario(usuario.getIdUsuarioSMA());
        long pendientes = reclamos.stream().filter(r -> "Pendiente".equalsIgnoreCase(r.getEstado())).count();
        long enAtencion = reclamos.stream().filter(r -> "En atención".equalsIgnoreCase(r.getEstado())).count();
        long resueltos = reclamos.stream().filter(r -> "Resuelto".equalsIgnoreCase(r.getEstado())).count();
        request.setAttribute("pendientes", pendientes);
        request.setAttribute("enAtencion", enAtencion);
        request.setAttribute("resueltos", resueltos);
        request.getRequestDispatcher("/VistaSMA/UsuarioSMA/PanelUsuarioSMA.jsp").forward(request, response);
    }

    private void mostrarFormularioReclamo(HttpServletRequest request, HttpServletResponse response, ClsUsuarioSMA usuario) throws ServletException, IOException {
        List<ClsCategoriaSMA> categorias = reclamoDAO.listarCategorias();
        request.setAttribute("categorias", categorias);
        request.setAttribute("fechaActual", LocalDate.now());
        request.getRequestDispatcher("/VistaSMA/UsuarioSMA/RegistrarReclamoSMA.jsp").forward(request, response);
    }

    private void registrarReclamo(HttpServletRequest request, HttpServletResponse response, ClsUsuarioSMA usuario) throws IOException, ServletException {
        String idCategoria = request.getParameter("idCategoriaSMA");
        String descripcion = request.getParameter("descripcion");
        String fecha = request.getParameter("fecha");

        if (idCategoria == null || descripcion == null || descripcion.isBlank()) {
            request.setAttribute("mensajeError", "Debe completar todos los campos obligatorios.");
            mostrarFormularioReclamo(request, response, usuario);
            return;
        }

        int categoriaSeleccionada;
        try {
            categoriaSeleccionada = Integer.parseInt(idCategoria);
        } catch (NumberFormatException ex) {
            request.setAttribute("mensajeError", "La categoría seleccionada no es válida.");
            mostrarFormularioReclamo(request, response, usuario);
            return;
        }

        LocalDate fechaReclamo;
        try {
            fechaReclamo = (fecha != null && !fecha.isBlank()) ? LocalDate.parse(fecha) : LocalDate.now();
        } catch (Exception ex) {
            request.setAttribute("mensajeError", "La fecha ingresada no es válida.");
            mostrarFormularioReclamo(request, response, usuario);
            return;
        }

        ClsReclamoSMA reclamo = new ClsReclamoSMA();
        reclamo.setIdUsuarioSMA(usuario.getIdUsuarioSMA());
        reclamo.setIdCategoriaSMA(categoriaSeleccionada);
        reclamo.setDescripcion(descripcion.trim());
        reclamo.setFecha(fechaReclamo);

        boolean registrado = reclamoDAO.registrarReclamo(reclamo);
        if (registrado) {
            request.getSession().setAttribute("mensajeExito", "Reclamo registrado correctamente.");
            response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA?accion=listar");
        } else {
            request.setAttribute("mensajeError", "No se pudo registrar el reclamo.");
            mostrarFormularioReclamo(request, response, usuario);
        }
    }

    private void listarReclamosUsuario(HttpServletRequest request, HttpServletResponse response, ClsUsuarioSMA usuario) throws ServletException, IOException {
        List<ClsReclamoSMA> reclamos = reclamoDAO.listarReclamosPorUsuario(usuario.getIdUsuarioSMA());
        request.setAttribute("reclamos", reclamos);
        request.getRequestDispatcher("/VistaSMA/UsuarioSMA/ListarReclamosSMA.jsp").forward(request, response);
    }

    private void mostrarHistorial(HttpServletRequest request, HttpServletResponse response, ClsUsuarioSMA usuario) throws ServletException, IOException {
        String idReclamo = request.getParameter("idReclamoSMA");
        if (idReclamo == null) {
            response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA?accion=listar");
            return;
        }
        int id = Integer.parseInt(idReclamo);
        ClsReclamoSMA reclamo = reclamoDAO.obtenerReclamoPorId(id);
        if (reclamo == null) {
            response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA?accion=listar");
            return;
        }

        if (!usuario.esAdministrador() && !reclamoDAO.reclamoPerteneceAUsuario(id, usuario.getIdUsuarioSMA())) {
            response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA?accion=listar");
            return;
        }

        List<ClsSeguimientoSMA> seguimientos = reclamoDAO.listarSeguimientosPorReclamo(id);
        request.setAttribute("reclamo", reclamo);
        request.setAttribute("seguimientos", seguimientos);
        String vista = usuario.esAdministrador()
                ? "/VistaSMA/AdminSMA/DetalleReclamoSMA.jsp"
                : "/VistaSMA/UsuarioSMA/HistorialReclamoSMA.jsp";
        request.getRequestDispatcher(vista).forward(request, response);
    }

    private void cargarPanelAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Long> resumenEstados = reclamoDAO.obtenerTotalesPorEstado();
        List<ClsResumenSMA> resumenCategoria = reclamoDAO.resumenPorCategoriaYEstado();
        request.setAttribute("resumenEstados", resumenEstados);
        request.setAttribute("resumenCategoria", resumenCategoria);
        request.getRequestDispatcher("/VistaSMA/AdminSMA/PanelAdminSMA.jsp").forward(request, response);
    }

    private void listarReclamosAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String estado = request.getParameter("estado");
        String categoria = request.getParameter("idCategoriaSMA");
        Integer idCategoria = null;
        if (categoria != null && !categoria.isBlank()) {
            try {
                idCategoria = Integer.valueOf(categoria);
            } catch (NumberFormatException ex) {
                request.setAttribute("mensajeError", "La categoría seleccionada no es válida.");
            }
        }
        List<ClsReclamoSMA> reclamos = reclamoDAO.listarReclamos(estado, idCategoria);
        List<ClsCategoriaSMA> categorias = reclamoDAO.listarCategorias();
        request.setAttribute("reclamos", reclamos);
        request.setAttribute("categorias", categorias);
        if (request.getAttribute("mensajeError") != null) {
            request.setAttribute("estadoSeleccionado", estado);
            request.setAttribute("categoriaSeleccionada", categoria);
        }
        request.getRequestDispatcher("/VistaSMA/AdminSMA/GestionReclamosSMA.jsp").forward(request, response);
    }

    private void mostrarDetalleAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idReclamo = request.getParameter("idReclamoSMA");
        if (idReclamo == null) {
            response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA?accion=gestion");
            return;
        }
        int id = Integer.parseInt(idReclamo);
        ClsReclamoSMA reclamo = reclamoDAO.obtenerReclamoPorId(id);
        if (reclamo == null) {
            response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA?accion=gestion");
            return;
        }
        List<ClsSeguimientoSMA> seguimientos = reclamoDAO.listarSeguimientosPorReclamo(id);
        request.setAttribute("reclamo", reclamo);
        request.setAttribute("seguimientos", seguimientos);
        request.getRequestDispatcher("/VistaSMA/AdminSMA/DetalleReclamoSMA.jsp").forward(request, response);
    }

    private void mostrarReportes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Long> resumenEstados = reclamoDAO.obtenerTotalesPorEstado();
        List<ClsResumenSMA> resumenCategoria = reclamoDAO.resumenPorCategoriaYEstado();
        request.setAttribute("resumenEstados", resumenEstados);
        request.setAttribute("resumenCategoria", resumenCategoria);
        request.getRequestDispatcher("/VistaSMA/AdminSMA/ReporteResumenSMA.jsp").forward(request, response);
    }

    private void actualizarEstado(HttpServletRequest request, HttpServletResponse response, ClsUsuarioSMA usuario) throws IOException {
        int idReclamo = Integer.parseInt(request.getParameter("idReclamoSMA"));
        String estado = request.getParameter("estado");
        String observacion = request.getParameter("observacion");
        if (observacion == null || observacion.isBlank()) {
            observacion = "Actualización sin observación";
        }
        reclamoDAO.actualizarEstadoReclamo(idReclamo, estado, usuario.getIdUsuarioSMA(), observacion.trim());
        request.getSession().setAttribute("mensajeExito", "Estado del reclamo actualizado.");
        response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA?accion=gestion");
    }

    private void registrarSeguimientoManual(HttpServletRequest request, HttpServletResponse response, ClsUsuarioSMA usuario) throws IOException {
        int idReclamo = Integer.parseInt(request.getParameter("idReclamoSMA"));
        String observacion = request.getParameter("observacion");
        String estado = request.getParameter("estado");
        if (estado == null || estado.isBlank()) {
            estado = "En atención";
        }
        if (observacion == null || observacion.isBlank()) {
            observacion = "Sin observaciones";
        }
        ClsSeguimientoSMA seguimiento = new ClsSeguimientoSMA();
        seguimiento.setIdReclamoSMA(idReclamo);
        seguimiento.setIdAdminSMA(usuario.getIdUsuarioSMA());
        seguimiento.setFecha(LocalDateTime.now());
        seguimiento.setObservacion(observacion.trim());
        seguimiento.setEstado(estado);
        reclamoDAO.registrarSeguimiento(seguimiento);
        request.getSession().setAttribute("mensajeExito", "Seguimiento registrado.");
        response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA?accion=detalle&idReclamoSMA=" + idReclamo);
    }
}
