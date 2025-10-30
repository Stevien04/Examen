package ControladorSMA;

import ModeloDAOSMA.ClsLoginSMA;
import ModeloSMA.ClsUsuarioSMA;
import java.io.IOException;
import java.security.SecureRandom;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@WebServlet(name = "ControladorLoginSMA", urlPatterns = {"/ControladorLoginSMA"})
public class ControladorLoginSMA extends HttpServlet {

    private static final String SESSION_USUARIO = "usuarioActual";
    private static final String SESSION_CAPTCHA = "captchaGenerado";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ClsLoginSMA loginDAO;

    public ControladorLoginSMA() {
        this.loginDAO = new ClsLoginSMA();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String accion = request.getParameter("accion");

        if ("logout".equalsIgnoreCase(accion)) {
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/ControladorLoginSMA");
            return;
        }

        if (session != null && session.getAttribute(SESSION_USUARIO) != null) {
            ClsUsuarioSMA usuario = (ClsUsuarioSMA) session.getAttribute(SESSION_USUARIO);
            redirigirPorRol(usuario, request, response);
            return;
        }

        session = request.getSession(true);
        String captcha = generarCaptcha();
        session.setAttribute(SESSION_CAPTCHA, captcha);
        request.setAttribute("captcha", captcha);
        request.getRequestDispatcher("/VistaSMA/LoginSMA.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String accion = request.getParameter("accion");

        if (!"login".equalsIgnoreCase(accion)) {
            response.sendRedirect(request.getContextPath() + "/ControladorLoginSMA");
            return;
        }

        String correo = request.getParameter("correo");
        String password = request.getParameter("password");
        String captchaIngresado = request.getParameter("captcha");
        String captchaEsperado = (String) session.getAttribute(SESSION_CAPTCHA);

        if (captchaEsperado == null || captchaIngresado == null || !captchaEsperado.equalsIgnoreCase(captchaIngresado.trim())) {
            prepararError("Captcha incorrecto. Intente nuevamente.", request, response);
            return;
        }

        ClsUsuarioSMA usuario = loginDAO.validarCredenciales(correo, password);
        if (usuario == null) {
            prepararError("Credenciales inválidas o usuario inactivo.", request, response);
            return;
        }

       String ipRemota = obtenerIPv4Cliente(request);
        if (usuario.getIpAutorizada() != null && !usuario.getIpAutorizada().isBlank()) {
            if (ipRemota == null || !usuario.getIpAutorizada().equals(ipRemota)) {
                String ipMostrada = ipRemota != null ? ipRemota : "desconocida";
                prepararError("Acceso denegado desde la IP " + ipMostrada + ".", request, response);
                return;
            }
        }

        session.setAttribute(SESSION_USUARIO, usuario);
        session.removeAttribute(SESSION_CAPTCHA);
        redirigirPorRol(usuario, request, response);
    }

    private void prepararError(String mensaje, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String nuevoCaptcha = generarCaptcha();
        session.setAttribute(SESSION_CAPTCHA, nuevoCaptcha);
        request.setAttribute("captcha", nuevoCaptcha);
        request.setAttribute("mensajeError", mensaje);
        request.getRequestDispatcher("/VistaSMA/LoginSMA.jsp").forward(request, response);
    }

    private void redirigirPorRol(ClsUsuarioSMA usuario, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (usuario.esAdministrador()) {
            response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA?accion=panelAdmin");
        } else {
            response.sendRedirect(request.getContextPath() + "/ControladorReclamoSMA?accion=panelUsuario");
        }
    }

    private String generarCaptcha() {
        String caracteres = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder builder = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            builder.append(caracteres.charAt(RANDOM.nextInt(caracteres.length())));
        }
        return builder.toString();
    }

   private String obtenerIPv4Cliente(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank()) {
            for (String candidato : ip.split(",")) {
                String ipv4 = normalizarIPv4(candidato.trim());
                if (ipv4 != null) {
                    return ipv4;
                }
            }
        }
        return normalizarIPv4(request.getRemoteAddr());
    }

    private String normalizarIPv4(String ip) {
        if (ip == null || ip.isBlank()) {
            return null;
        }
        try {
            InetAddress direccion = InetAddress.getByName(ip);
            if (direccion instanceof Inet4Address) {
                return direccion.getHostAddress();
            }

            byte[] bytes = direccion.getAddress();
            if (bytes.length == 16) {
                boolean mapeada = true;
                for (int i = 0; i < 10; i++) {
                    if (bytes[i] != 0) {
                        mapeada = false;
                        break;
                    }
                }
                if (mapeada && bytes[10] == (byte) 0xff && bytes[11] == (byte) 0xff) {
                    return String.format("%d.%d.%d.%d",
                            bytes[12] & 0xff,
                            bytes[13] & 0xff,
                            bytes[14] & 0xff,
                            bytes[15] & 0xff);
                }
            }
        } catch (UnknownHostException ex) {
            return null;
        }
       return null;
    }
}