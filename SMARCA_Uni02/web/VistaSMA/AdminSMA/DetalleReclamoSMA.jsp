<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Detalle del Reclamo</title>
        <style>
            body { font-family: 'Segoe UI', sans-serif; background: #f5f6fa; margin: 0; }
            header { background: #111827; color: #fff; padding: 22px; }
            header a { color: #fff; margin-right: 18px; text-decoration: none; font-weight: bold; }
            .container { max-width: 960px; margin: 30px auto; background: #fff; padding: 30px; border-radius: 12px; box-shadow: 0 12px 25px rgba(0,0,0,0.08); }
            h2 { margin-top: 0; }
            .detalle { background: #f9fafb; padding: 18px; border-radius: 8px; margin-bottom: 24px; }
            table { width: 100%; border-collapse: collapse; }
            th, td { padding: 12px 14px; border-bottom: 1px solid #e5e7eb; text-align: left; }
            th { background: #f3f4f6; color: #1f2937; }
            textarea { width: 100%; min-height: 100px; border-radius: 8px; border: 1px solid #cbd5f5; padding: 10px; }
            button { padding: 12px 18px; background: #2563eb; color: #fff; border: none; border-radius: 6px; font-weight: bold; cursor: pointer; }
        </style>
    </head>
    <body>
        <header>
            <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=panelAdmin">Inicio</a>
            <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=gestion">Gestión de reclamos</a>
            <a href="${pageContext.request.contextPath}/ControladorUsuarioSMA">Usuarios</a>
            <a href="${pageContext.request.contextPath}/ControladorLoginSMA?accion=logout">Salir</a>
        </header>
        <div class="container">
            <h2>Reclamo #${reclamo.idReclamoSMA}</h2>
            <div class="detalle">
                <p><strong>Usuario:</strong> ${reclamo.nombreUsuario}</p>
                <p><strong>Categoría:</strong> ${reclamo.nombreCategoria}</p>
                <p><strong>Descripción:</strong> ${reclamo.descripcion}</p>
                <p><strong>Estado actual:</strong> ${reclamo.estado}</p>
                <p><strong>Fecha:</strong> ${reclamo.fecha}</p>
            </div>
            <h3>Historial de seguimiento</h3>
            <c:choose>
                <c:when test="${empty seguimientos}">
                    <p>No se han registrado seguimientos.</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>Fecha</th>
                                <th>Estado</th>
                                <th>Responsable</th>
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
            <h3>Registrar nueva acción</h3>
            <form action="${pageContext.request.contextPath}/ControladorReclamoSMA" method="post">
                <input type="hidden" name="accion" value="agregarSeguimiento" />
                <input type="hidden" name="idReclamoSMA" value="${reclamo.idReclamoSMA}" />
                <label>Estado
                    <select name="estado" required>
                        <option value="En atención">En atención</option>
                        <option value="Resuelto">Resuelto</option>
                    </select>
                </label>
                <label>Observación
                    <textarea name="observacion" placeholder="Detalle la actividad realizada" required></textarea>
                </label>
                <button type="submit">Guardar seguimiento</button>
            </form>
        </div>
    </body>
</html>