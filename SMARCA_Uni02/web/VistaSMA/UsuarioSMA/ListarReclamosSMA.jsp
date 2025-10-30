<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Mis Reclamos</title>
        <style>
            body { font-family: Arial, sans-serif; background: #f8fafc; margin: 0; }
            header { background: #1a73e8; color: #fff; padding: 20px; }
            header a { color: #fff; margin-right: 16px; text-decoration: none; font-weight: bold; }
            .container { padding: 28px; }
            table { width: 100%; border-collapse: collapse; background: #fff; box-shadow: 0 10px 25px rgba(0,0,0,0.08); }
            th, td { padding: 14px 16px; border-bottom: 1px solid #edf2f7; text-align: left; }
            th { background: #f1f5f9; color: #2c3e50; }
            tr:hover { background: #f8fbff; }
            .estado { font-weight: bold; border-radius: 12px; padding: 6px 12px; display: inline-block; }
            .estado-pendiente { background: #fff4e5; color: #c77b00; }
            .estado-en-atencion { background: #e0f3ff; color: #0969da; }
            .estado-resuelto { background: #e6f4ea; color: #1e8e3e; }
            .acciones a { color: #1a73e8; text-decoration: none; font-weight: bold; }
            .acciones a:hover { text-decoration: underline; }
            .mensaje { padding: 16px; border-radius: 8px; margin-bottom: 20px; font-weight: bold; }
            .exito { background: #e6f4ea; color: #237804; }
        </style>
    </head>
    <body>
        <header>
            <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=panelUsuario">Inicio</a>
            <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=nuevo">Registrar reclamo</a>
            <a href="${pageContext.request.contextPath}/ControladorLoginSMA?accion=logout">Salir</a>
        </header>
        <div class="container">
            <h2>Reclamos registrados</h2>
            <c:if test="${not empty sessionScope.mensajeExito}">
                <div class="mensaje exito">${sessionScope.mensajeExito}</div>
                <c:remove var="mensajeExito" scope="session" />
            </c:if>
            <c:choose>
                <c:when test="${empty reclamos}">
                    <p>No se encontraron reclamos registrados.</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>Código</th>
                                <th>Categoría</th>
                                <th>Descripción</th>
                                <th>Fecha</th>
                                <th>Estado</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="reclamo" items="${reclamos}">
                                <tr>
                                    <td>#${reclamo.idReclamoSMA}</td>
                                    <td>${reclamo.nombreCategoria}</td>
                                    <td>${reclamo.descripcion}</td>
                                    <td>${reclamo.fecha}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${reclamo.estado eq 'Pendiente'}">
                                                <span class="estado estado-pendiente">${reclamo.estado}</span>
                                            </c:when>
                                            <c:when test="${reclamo.estado eq 'En atención'}">
                                                <span class="estado estado-en-atencion">${reclamo.estado}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="estado estado-resuelto">${reclamo.estado}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="acciones">
                                        <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=historial&idReclamoSMA=${reclamo.idReclamoSMA}">Ver seguimiento</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>