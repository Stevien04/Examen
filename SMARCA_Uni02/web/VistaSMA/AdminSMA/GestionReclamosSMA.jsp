<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Reclamos</title>
    <style>
        body { font-family: 'Segoe UI', sans-serif; background: #f1f5f9; margin: 0; }
        header { background: #111827; color: #fff; padding: 22px; }
        header a { color: #fff; margin-right: 18px; text-decoration: none; font-weight: bold; }
        .container { padding: 28px; }
        .filtros { display: flex; gap: 16px; flex-wrap: wrap; margin-bottom: 20px; }
        select { padding: 10px 12px; border: 1px solid #cbd5f5; border-radius: 6px; }
        button { padding: 10px 18px; border: none; background: #2563eb; color: #fff; border-radius: 6px; cursor: pointer; font-weight: bold; }
        table { width: 100%; border-collapse: collapse; background: #fff; box-shadow: 0 12px 25px rgba(0,0,0,0.08); }
        th, td { padding: 14px 16px; border-bottom: 1px solid #e5e7eb; text-align: left; vertical-align: top; }
        th { background: #f3f4f6; color: #111827; }
        .form-accion { margin-top: 12px; background: #f9fafb; padding: 12px; border-radius: 8px; }
        textarea { width: 100%; min-height: 80px; border-radius: 6px; border: 1px solid #cbd5f5; padding: 10px; }
        .mensaje { padding: 16px; border-radius: 8px; margin-bottom: 20px; font-weight: bold; }
        .exito { background: #e6f4ea; color: #237804; }
        .error { background: #fdecea; color: #c0392b; }
    </style>
</head>
<body>
<header>
    <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=panelAdmin">Inicio</a>
    <a href="${pageContext.request.contextPath}/ControladorUsuarioSMA">Usuarios y roles</a>
    <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=reportes">Reportes</a>
    <a href="${pageContext.request.contextPath}/ControladorLoginSMA?accion=logout">Salir</a>
</header>

<div class="container">
    <h2>Gestión de reclamos</h2>

    <c:if test="${not empty sessionScope.mensajeExito}">
        <div class="mensaje exito">${sessionScope.mensajeExito}</div>
        <c:remove var="mensajeExito" scope="session" />
    </c:if>

    <c:if test="${not empty mensajeError}">
        <div class="mensaje error">${mensajeError}</div>
    </c:if>

    <form class="filtros" action="${pageContext.request.contextPath}/ControladorReclamoSMA" method="get">
        <input type="hidden" name="accion" value="gestion" />
        <label>
            Estado
            <select name="estado">
                <option value="">Todos</option>
                <option value="Pendiente" <c:if test="${param.estado eq 'Pendiente'}">selected</c:if>>Pendiente</option>
                <option value="En atención" <c:if test="${param.estado eq 'En atención'}">selected</c:if>>En atención</option>
                <option value="Resuelto" <c:if test="${param.estado eq 'Resuelto'}">selected</c:if>>Resuelto</option>
            </select>
        </label>

        <label>
            Categoría
            <select name="idCategoriaSMA">
                <option value="">Todas</option>
                <c:forEach var="categoria" items="${categorias}">
                    <option value="${categoria.idCategoriaSMA}" <c:if test="${param.idCategoriaSMA eq categoria.idCategoriaSMA}">selected</c:if>>${categoria.nombreCategoria}</option>
                </c:forEach>
            </select>
        </label>

        <button type="submit">Filtrar</button>
    </form>

    <table>
        <thead>
            <tr>
                <th>Código</th>
                <th>Usuario</th>
                <th>Categoría</th>
                <th>Fecha</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:if test="${empty reclamos}">
                <tr><td colspan="6">No se encontraron reclamos con los filtros seleccionados.</td></tr>
            </c:if>

            <c:forEach var="reclamo" items="${reclamos}">
                <tr>
                    <td>#${reclamo.idReclamoSMA}</td>
                    <td>${reclamo.nombreUsuario}</td>
                    <td>${reclamo.nombreCategoria}</td>
                    <td>${reclamo.fecha}</td>
                    <td>${reclamo.estado}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=detalle&idReclamoSMA=${reclamo.idReclamoSMA}">Ver detalle</a>
                        <div class="form-accion">
                            <form action="${pageContext.request.contextPath}/ControladorReclamoSMA" method="post">
                                <input type="hidden" name="accion" value="actualizarEstado" />
                                <input type="hidden" name="idReclamoSMA" value="${reclamo.idReclamoSMA}" />
                                <label>
                                    Estado
                                    <select name="estado" required>
                                        <option value="Pendiente" <c:if test="${reclamo.estado eq 'Pendiente'}">selected</c:if>>Pendiente</option>
                                        <option value="En atención" <c:if test="${reclamo.estado eq 'En atención'}">selected</c:if>>En atención</option>
                                        <option value="Resuelto" <c:if test="${reclamo.estado eq 'Resuelto'}">selected</c:if>>Resuelto</option>
                                    </select>
                                </label>
                                <label>Observación
                                    <textarea name="observacion" placeholder="Describa la acción realizada"></textarea>
                                </label>
                                <button type="submit">Actualizar</button>
                            </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
