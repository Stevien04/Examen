<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Reporte de Reclamos</title>
        <style>
            body { font-family: 'Segoe UI', sans-serif; background: #f9fafb; margin: 0; }
            header { background: #111827; color: #fff; padding: 22px; }
            header a { color: #fff; margin-right: 18px; text-decoration: none; font-weight: bold; }
            .container { padding: 30px; }
            .card { background: #fff; border-radius: 12px; padding: 24px; box-shadow: 0 12px 25px rgba(0,0,0,0.08); margin-bottom: 24px; }
            h2 { margin-top: 0; }
            .resumen-estados { display: flex; gap: 16px; flex-wrap: wrap; }
            .resumen-estados .item { background: #2563eb; color: #fff; padding: 16px; border-radius: 10px; min-width: 160px; }
            table { width: 100%; border-collapse: collapse; margin-top: 16px; }
            th, td { padding: 12px 14px; border-bottom: 1px solid #e5e7eb; text-align: left; }
            th { background: #f3f4f6; color: #111827; }
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
            <div class="card">
                <h2>Resumen por estado</h2>
                <div class="resumen-estados">
                    <c:forEach var="entry" items="${resumenEstados}">
                        <div class="item">
                            <strong>${entry.key}</strong>
                            <div>${entry.value} reclamos</div>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <div class="card">
                <h2>Resumen por categoría y estado</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Categoría</th>
                            <th>Estado</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${resumenCategoria}">
                            <tr>
                                <td>${item.categoria}</td>
                                <td>${item.estado}</td>
                                <td>${item.total}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>