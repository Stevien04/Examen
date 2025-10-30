<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Gestión de Usuarios</title>
        <style>
            body { font-family: 'Segoe UI', sans-serif; background: #f5f6fa; margin: 0; }
            header { background: #111827; color: #fff; padding: 22px; }
            header a { color: #fff; margin-right: 18px; text-decoration: none; font-weight: bold; }
            .container { padding: 30px; }
            .acciones { margin-bottom: 20px; }
            .acciones a { display: inline-block; background: #2563eb; color: #fff; padding: 12px 18px; border-radius: 6px; text-decoration: none; font-weight: bold; }
            table { width: 100%; border-collapse: collapse; background: #fff; box-shadow: 0 12px 25px rgba(0,0,0,0.08); }
            th, td { padding: 14px 16px; border-bottom: 1px solid #e5e7eb; text-align: left; }
            th { background: #f3f4f6; color: #1f2937; }
            .acciones-tabla a { color: #2563eb; font-weight: bold; text-decoration: none; margin-right: 12px; }
            .acciones-tabla a:hover { text-decoration: underline; }
            .mensaje { padding: 16px; border-radius: 8px; margin-bottom: 20px; font-weight: bold; }
            .exito { background: #e6f4ea; color: #237804; }
        </style>
    </head>
    <body>
        <header>
            <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=panelAdmin">Inicio</a>
            <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=gestion">Reclamos</a>
            <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=reportes">Reportes</a>
            <a href="${pageContext.request.contextPath}/ControladorLoginSMA?accion=logout">Salir</a>
        </header>
        <div class="container">
            <h2>Usuarios y roles</h2>
            <c:if test="${not empty sessionScope.mensajeExito}">
                <div class="mensaje exito">${sessionScope.mensajeExito}</div>
                <c:remove var="mensajeExito" scope="session" />
            </c:if>
            <div class="acciones">
                <a href="${pageContext.request.contextPath}/ControladorUsuarioSMA?accion=nuevo">Crear nuevo usuario</a>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Correo</th>
                        <th>Rol</th>
                        <th>IP autorizada</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="usuario" items="${usuarios}">
                        <tr>
                            <td>${usuario.idUsuarioSMA}</td>
                            <td>${usuario.nombre}</td>
                            <td>${usuario.correo}</td>
                            <td>${usuario.nombreRol}</td>
                            <td>${empty usuario.ipAutorizada ? 'No asignada' : usuario.ipAutorizada}</td>
                            <td><c:out value="${usuario.activo ? 'Activo' : 'Inactivo'}" /></td>
                            <td class="acciones-tabla">
                                <a href="${pageContext.request.contextPath}/ControladorUsuarioSMA?accion=editar&idUsuarioSMA=${usuario.idUsuarioSMA}">Editar</a>
                                <a href="${pageContext.request.contextPath}/ControladorUsuarioSMA?accion=eliminar&idUsuarioSMA=${usuario.idUsuarioSMA}" onclick="return confirm('¿Confirma eliminar al usuario?');">Eliminar</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>