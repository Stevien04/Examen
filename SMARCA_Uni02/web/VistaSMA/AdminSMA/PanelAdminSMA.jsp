<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Panel del Administrador</title>
        <style>
            body { font-family: 'Segoe UI', sans-serif; background: #f4f6f9; margin: 0; }
            header { background: #111827; color: #fff; padding: 22px; }
            header h1 { margin: 0; }
            nav a { color: #fff; margin-right: 18px; text-decoration: none; font-weight: bold; }
            .container { padding: 32px; }
            .resumen { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 20px; }
            .card { background: #fff; padding: 24px; border-radius: 12px; box-shadow: 0 12px 30px rgba(0,0,0,0.08); }
            .card h2 { margin: 0 0 12px; color: #374151; }
            .card p { font-size: 2rem; font-weight: bold; color: #2563eb; margin: 0; }
            .tabla-resumen { margin-top: 32px; background: #fff; border-radius: 12px; padding: 24px; box-shadow: 0 12px 25px rgba(0,0,0,0.08); }
            table { width: 100%; border-collapse: collapse; }
            th, td { padding: 12px; border-bottom: 1px solid #e5e7eb; text-align: left; }
            th { background: #f3f4f6; color: #374151; }
        </style>
    </head>
    <body>
        <header>
            <h1>Panel de Administración</h1>
            <nav>
                <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=panelAdmin">Inicio</a>
                <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=gestion">Gestionar reclamos</a>
                <a href="${pageContext.request.contextPath}/ControladorUsuarioSMA">Usuarios y roles</a>
                <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=reportes">Reportes</a>
                <a href="${pageContext.request.contextPath}/ControladorLoginSMA?accion=logout">Cerrar sesión</a>
            </nav>
        </header>
        <div class="container">
            <div class="resumen">
                <c:forEach var="entry" items="${resumenEstados}">
                    <div class="card">
                        <h2>${entry.key}</h2>
                        <p>${entry.value}</p>
                    </div>
                </c:forEach>
            </div>
            <div class="tabla-resumen">
                <h2>Reclamos por categoría y estado</h2>
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