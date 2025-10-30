<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Historial del Reclamo</title>
        <style>
            body { font-family: Arial, sans-serif; background: #f5f6fa; margin: 0; }
            header { background: #1a73e8; color: #fff; padding: 20px; }
            header a { color: #fff; margin-right: 16px; text-decoration: none; font-weight: bold; }
            .container { max-width: 900px; margin: 30px auto; background: #fff; padding: 28px; border-radius: 12px; box-shadow: 0 12px 25px rgba(0,0,0,0.1); }
            h2 { margin-top: 0; }
            .detalle { background: #f1f5f9; padding: 16px; border-radius: 8px; margin-bottom: 24px; }
            table { width: 100%; border-collapse: collapse; }
            th, td { padding: 12px 14px; border-bottom: 1px solid #e2e8f0; text-align: left; }
            th { background: #f8fafc; color: #2c3e50; }
            .empty { text-align: center; padding: 30px; color: #6b7280; }
        </style>
    </head>
    <body>
        <header>
            <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=panelUsuario">Inicio</a>
            <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=listar">Mis reclamos</a>
            <a href="${pageContext.request.contextPath}/ControladorLoginSMA?accion=logout">Salir</a>
        </header>
        <div class="container">
            <h2>Seguimiento del reclamo #${reclamo.idReclamoSMA}</h2>
            <div class="detalle">
                <p><strong>Categoría:</strong> ${reclamo.nombreCategoria}</p>
                <p><strong>Descripción:</strong> ${reclamo.descripcion}</p>
                <p><strong>Estado actual:</strong> ${reclamo.estado}</p>
                <p><strong>Fecha de registro:</strong> ${reclamo.fecha}</p>
            </div>
            <h3>Historial de atención</h3>
            <c:choose>
                <c:when test="${empty seguimientos}">
                    <div class="empty">Aún no se registran acciones sobre este reclamo.</div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>Fecha</th>
                                <th>Estado</th>
                                <th>Atendido por</th>
                                <th>Observación</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${seguimientos}">
                                <tr>
                                    <td>${item.fecha}</td>
                                    <td>${item.estado}</td>
                                    <td><c:out value="${empty item.nombreAdmin ? 'Sin asignar' : item.nombreAdmin}" /></td>
                                    <td>${item.observacion}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>