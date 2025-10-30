package ControladorSMA;

import ModeloDAOSMA.ClsUsuarioDAOSMA;
import ModeloSMA.ClsRolSMA;
import ModeloSMA.ClsUsuarioSMA;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ControladorUsuarioSMA", urlPatterns = {"/ControladorUsuarioSMA"})
public class ControladorUsuarioSMA extends HttpServlet {

    private final ClsUsuarioDAOSMA usuarioDAO;

    public ControladorUsuarioSMA() {
        this.usuarioDAO = new ClsUsuarioDAOSMA();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioActual") == null) {
            response.sendRedirect(request.getContextPath() + "/ControladorLoginSMA");
            return;
        }

        ClsUsuarioSMA usuario = (ClsUsuarioSMA) session.getAttribute("usuarioActual");
        if (!usuario.esAdministrador()) {
            response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA");
            return;
        }

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "nuevo":
                ClsUsuarioSMA nuevoUsuario = new ClsUsuarioSMA();
                nuevoUsuario.setActivo(true);
                mostrarFormulario(request, response, nuevoUsuario, false);
                break;
            case "editar":
                mostrarFormularioEdicion(request, response);
                break;
            case "eliminar":
                eliminarUsuario(request, response);
                break;
            default:
                listarUsuarios(request, response);
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
        if (!usuario.esAdministrador()) {
            response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA");
            return;
        }

        String accion = request.getParameter("accion");
        if ("guardar".equalsIgnoreCase(accion)) {
            guardarUsuario(request, response);
        } else if ("actualizar".equalsIgnoreCase(accion)) {
            actualizarUsuario(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/ControladorUsuarioSMA");
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ClsUsuarioSMA> usuarios = usuarioDAO.listarUsuarios();
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("/VistaSMA/AdminSMA/GestionUsuariosSMA.jsp").forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, ClsUsuarioSMA usuario, boolean edicion) throws ServletException, IOException {
        List<ClsRolSMA> roles = usuarioDAO.listarRoles();
        request.setAttribute("roles", roles);
        request.setAttribute("usuarioEditar", usuario);
        request.setAttribute("esEdicion", edicion);
        request.getRequestDispatcher("/VistaSMA/AdminSMA/FormularioUsuarioSMA.jsp").forward(request, response);
    }

    private void mostrarFormularioEdicion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("idUsuarioSMA");
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/ControladorUsuarioSMA");
            return;
        }
        ClsUsuarioSMA usuario = usuarioDAO.obtenerUsuarioPorId(Integer.parseInt(id));
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/ControladorUsuarioSMA");
            return;
        }
        mostrarFormulario(request, response, usuario, true);
    }

    private void guardarUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ClsUsuarioSMA usuario = construirUsuarioDesdeRequest(request);
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()
                || usuario.getCorreo() == null || usuario.getCorreo().isBlank()
                || usuario.getPassword() == null || usuario.getPassword().isBlank()
                || usuario.getIdRolSMA() <= 0) {
            request.setAttribute("mensajeError", "Todos los campos son obligatorios.");
            mostrarFormulario(request, response, usuario, false);
            return;
        }
        usuarioDAO.registrarUsuario(usuario);
        request.getSession().setAttribute("mensajeExito", "Usuario registrado correctamente.");
        response.sendRedirect(request.getContextPath() + "/ControladorUsuarioSMA");
    }

    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ClsUsuarioSMA usuario = construirUsuarioDesdeRequest(request);
        usuario.setIdUsuarioSMA(Integer.parseInt(request.getParameter("idUsuarioSMA")));
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()
                || usuario.getCorreo() == null || usuario.getCorreo().isBlank()
                || usuario.getPassword() == null || usuario.getPassword().isBlank()
                || usuario.getIdRolSMA() <= 0) {
            request.setAttribute("mensajeError", "Todos los campos son obligatorios.");
            mostrarFormulario(request, response, usuario, true);
            return;
        }
        usuarioDAO.actualizarUsuario(usuario);
        request.getSession().setAttribute("mensajeExito", "Usuario actualizado correctamente.");
        response.sendRedirect(request.getContextPath() + "/ControladorUsuarioSMA");
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("idUsuarioSMA");
        if (id != null) {
            usuarioDAO.eliminarUsuario(Integer.parseInt(id));
            request.getSession().setAttribute("mensajeExito", "Usuario eliminado correctamente.");
        }
        response.sendRedirect(request.getContextPath() + "/ControladorUsuarioSMA");
    }

    private ClsUsuarioSMA construirUsuarioDesdeRequest(HttpServletRequest request) {
        ClsUsuarioSMA usuario = new ClsUsuarioSMA();
        usuario.setNombre(request.getParameter("nombre"));
        usuario.setCorreo(request.getParameter("correo"));
        usuario.setPassword(request.getParameter("password"));
        int idRol = 0;
        try {
            idRol = Integer.parseInt(request.getParameter("idRolSMA"));
        } catch (NumberFormatException ex) {
            idRol = 0;
        }
        usuario.setIdRolSMA(idRol);
        usuario.setIpAutorizada(request.getParameter("ipAutorizada"));
        usuario.setActivo("on".equalsIgnoreCase(request.getParameter("activo")) || "true".equalsIgnoreCase(request.getParameter("activo")));
        return usuario;
    }
}
