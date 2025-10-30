<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registrar Reclamo</title>
        <style>
            body { font-family: 'Segoe UI', sans-serif; background: #eef2f7; margin: 0; }
            header { background: #1a73e8; color: #fff; padding: 20px; }
            header a { color: #fff; margin-right: 18px; text-decoration: none; font-weight: bold; }
            .container { max-width: 720px; margin: 30px auto; background: #fff; padding: 30px; border-radius: 12px; box-shadow: 0 15px 30px rgba(0,0,0,0.1); }
            h2 { margin-top: 0; color: #2c3e50; }
            label { display: block; margin-bottom: 8px; font-weight: bold; color: #2c3e50; }
            select, textarea, input[type="date"] { width: 100%; padding: 12px; border: 1px solid #ccd1d9; border-radius: 6px; margin-bottom: 16px; }
            textarea { min-height: 140px; resize: vertical; }
            button { background: #1a73e8; color: #fff; border: none; padding: 12px 20px; border-radius: 6px; font-size: 1rem; cursor: pointer; }
            button:hover { background: #155bb5; }
            .mensaje-error { background: #fdecea; color: #c0392b; padding: 12px; border-radius: 6px; margin-bottom: 18px; }
        </style>
    </head>
    <body>
        <header>
            <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=panelUsuario">Inicio</a>
            <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=listar">Mis reclamos</a>
            <a href="${pageContext.request.contextPath}/ControladorLoginSMA?accion=logout">Salir</a>
        </header>
        <div class="container">
            <h2>Registrar un nuevo reclamo</h2>
            <p>Complete el formulario con la mayor cantidad de detalles posible para agilizar la atención.</p>
            <c:if test="${not empty mensajeError}">
                <div class="mensaje-error">${mensajeError}</div>
            </c:if>
            <form action="${pageContext.request.contextPath}/ControladorReclamoSMA" method="post">
                <input type="hidden" name="accion" value="registrar" />
                <label for="idCategoriaSMA">Categoría</label>
                <select id="idCategoriaSMA" name="idCategoriaSMA" required>
                    <option value="" disabled selected>Seleccione la categoría</option>
                    <c:forEach var="categoria" items="${categorias}">
                        <option value="${categoria.idCategoriaSMA}">${categoria.nombreCategoria}</option>
                    </c:forEach>
                </select>
                <label for="descripcion">Descripción</label>
                <textarea id="descripcion" name="descripcion" placeholder="Describa el problema" required></textarea>
                <label for="fecha">Fecha del reclamo</label>
                <input type="date" id="fecha" name="fecha" value="${fechaActual}" required />
                <button type="submit">Guardar reclamo</button>
            </form>
        </div>
    </body>
</html>