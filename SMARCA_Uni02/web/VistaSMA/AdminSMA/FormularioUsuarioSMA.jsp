<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>
            <c:choose>
                <c:when test="${esEdicion}">Editar usuario</c:when>
                <c:otherwise>Nuevo usuario</c:otherwise>
            </c:choose>
        </title>
        <style>
            body { font-family: 'Segoe UI', sans-serif; background: #f5f6fa; margin: 0; }
            header { background: #111827; color: #fff; padding: 22px; }
            header a { color: #fff; margin-right: 18px; text-decoration: none; font-weight: bold; }
            .container { max-width: 700px; margin: 30px auto; background: #fff; padding: 30px; border-radius: 12px; box-shadow: 0 12px 25px rgba(0,0,0,0.08); }
            h2 { margin-top: 0; }
            label { display: block; margin-bottom: 8px; font-weight: bold; color: #1f2937; }
            input[type="text"], input[type="email"], input[type="password"], select { width: 100%; padding: 12px; border-radius: 6px; border: 1px solid #cbd5f5; margin-bottom: 16px; }
            .form-group { margin-bottom: 16px; }
            button { background: #2563eb; color: #fff; border: none; padding: 12px 20px; border-radius: 6px; font-weight: bold; cursor: pointer; }
            button:hover { background: #1d4ed8; }
            .checkbox { display: flex; align-items: center; gap: 10px; }
            .mensaje-error { background: #fdecea; color: #c0392b; padding: 12px; border-radius: 6px; margin-bottom: 18px; }
        </style>
    </head>
    <body>
        <header>
            <a href="${pageContext.request.contextPath}/ControladorUsuarioSMA">Volver</a>
            <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=panelAdmin">Panel</a>
            <a href="${pageContext.request.contextPath}/ControladorLoginSMA?accion=logout">Salir</a>
        </header>
        <div class="container">
            <h2>
                <c:choose>
                    <c:when test="${esEdicion}">Editar usuario</c:when>
                    <c:otherwise>Registrar nuevo usuario</c:otherwise>
                </c:choose>
            </h2>
            <c:if test="${not empty mensajeError}">
                <div class="mensaje-error">${mensajeError}</div>
            </c:if>
            <form action="${pageContext.request.contextPath}/ControladorUsuarioSMA" method="post">
                <input type="hidden" name="accion" value="${esEdicion ? 'actualizar' : 'guardar'}" />
                <c:if test="${esEdicion}">
                    <input type="hidden" name="idUsuarioSMA" value="${usuarioEditar.idUsuarioSMA}" />
                </c:if>
                <div class="form-group">
                    <label for="nombre">Nombre completo</label>
                    <input type="text" id="nombre" name="nombre" value="${usuarioEditar.nombre}" required />
                </div>
                <div class="form-group">
                    <label for="correo">Correo institucional</label>
                    <input type="email" id="correo" name="correo" value="${usuarioEditar.correo}" required />
                </div>
                <div class="form-group">
                    <label for="password">Contraseña</label>
                    <input type="password" id="password" name="password" value="${usuarioEditar.password}" required />
                </div>
                <div class="form-group">
                    <label for="idRolSMA">Rol</label>
                    <select id="idRolSMA" name="idRolSMA" required>
                        <c:forEach var="rol" items="${roles}">
                            <option value="${rol.idRolSMA}" <c:if test="${usuarioEditar.idRolSMA eq rol.idRolSMA}">selected</c:if>>${rol.nombreRol}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="ipAutorizada">IP autorizada</label>
                    <input type="text" id="ipAutorizada" name="ipAutorizada" value="${usuarioEditar.ipAutorizada}" placeholder="Ej. 192.168.1.10" />
                </div>
                <div class="form-group checkbox">
                    <input type="checkbox" id="activo" name="activo" <c:if test="${usuarioEditar.activo}">checked</c:if> />
                    <label for="activo">Usuario activo</label>
                </div>
                <button type="submit">Guardar</button>
            </form>
        </div>
    </body>
</html>