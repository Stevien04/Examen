<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Panel del Usuario</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/8.0.1/normalize.min.css" integrity="sha512-NhSC1YmyruXifcj/KFRWoC561Y8CbnZ4x3vpZl9YwSvv2zWnUw1DpFPdC5hZGp5CqL7n1H09G4w8wKkLu4l9UQ==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <style>
            body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f4f6f9; margin: 0; }
            header { background: #1a73e8; color: #fff; padding: 20px; }
            header h1 { margin: 0; font-size: 1.8rem; }
            nav a { color: #fff; margin-right: 18px; text-decoration: none; font-weight: bold; }
            .container { padding: 30px; }
            .resumen { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 20px; }
            .resumen .card { background: #fff; border-radius: 12px; padding: 24px; box-shadow: 0 10px 20px rgba(0,0,0,0.08); }
            .card h2 { margin: 0 0 10px; color: #2c3e50; font-size: 1.2rem; }
            .card p { margin: 0; font-size: 2rem; font-weight: bold; color: #1a73e8; }
            .acciones { margin-top: 30px; display: flex; flex-wrap: wrap; gap: 12px; }
            .acciones a { background: #1a73e8; color: #fff; padding: 12px 18px; border-radius: 6px; text-decoration: none; font-weight: bold; transition: background .2s ease; }
            .acciones a:hover { background: #155bb5; }
            .mensaje { padding: 16px; border-radius: 8px; margin-bottom: 20px; font-weight: bold; }
            .exito { background: #e6f4ea; color: #237804; }
        </style>
    </head>
    <body>
        <header>
            <h1>Bienvenido(a) al Portal de Reclamos</h1>
            <nav>
                <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=panelUsuario">Inicio</a>
                <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=nuevo">Registrar reclamo</a>
                <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=listar">Mis reclamos</a>
                <a href="${pageContext.request.contextPath}/ControladorLoginSMA?accion=logout">Cerrar sesión</a>
            </nav>
        </header>
        <div class="container">
            <c:if test="${not empty sessionScope.mensajeExito}">
                <div class="mensaje exito">${sessionScope.mensajeExito}</div>
                <c:remove var="mensajeExito" scope="session" />
            </c:if>
            <div class="resumen">
                <div class="card">
                    <h2>Reclamos pendientes</h2>
                    <p>${pendientes}</p>
                </div>
                <div class="card">
                    <h2>En atención</h2>
                    <p>${enAtencion}</p>
                </div>
                <div class="card">
                    <h2>Resueltos</h2>
                    <p>${resueltos}</p>
                </div>
            </div>
            <div class="acciones">
                <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=nuevo">Registrar nuevo reclamo</a>
                <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=listar">Ver seguimiento de mis reclamos</a>
            </div>
        </div>
    </body>
</html>