<%-- 
    Document   : Login
    Created on : 29 oct 2025, 18:36:32
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<html lang="es">
    <head>
      
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Ingreso al Sistema de Reclamos</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background: #f5f6fa;
                display: flex;
                justify-content: center;
                align-items: center;
                min-height: 100vh;
                margin: 0;
            }

            .sma-card {
                background: #ffffff;
                border-radius: 12px;
                box-shadow: 0 10px 35px rgba(0, 0, 0, 0.1);
                width: 100%;
                max-width: 420px;
                padding: 32px;
            }

            .sma-card h1 {
                margin-top: 0;
                color: #2c3e50;
                text-align: center;
            }

            .sma-card label {
                display: block;
                font-weight: bold;
                margin: 16px 0 6px;
                color: #34495e;
            }

            .sma-card input[type="text"],
            .sma-card input[type="password"] {
                width: 100%;
                padding: 10px 12px;
                border-radius: 6px;
                border: 1px solid #ccd1d9;
                box-sizing: border-box;
            }

            .sma-card .captcha-box {
                background: #f0f3f6;
                border-radius: 6px;
                padding: 12px;
                text-align: center;
                font-weight: bold;
                letter-spacing: 4px;
                font-size: 1.2rem;
                margin-bottom: 12px;
                color: #1a73e8;
            }

            .sma-card button {
                margin-top: 24px;
                width: 100%;
                padding: 12px;
                border: none;
                border-radius: 6px;
                background: #1a73e8;
                color: #ffffff;
                font-weight: bold;
                cursor: pointer;
                font-size: 1rem;
            }

            .sma-card button:hover {
                background: #155bb5;
            }

            .mensaje-error {
                background: #fdecea;
                color: #c0392b;
                padding: 12px;
                border-radius: 6px;
                margin-bottom: 16px;
                font-size: 0.95rem;
            }

            .mensaje-info {
                background: #e8f0fe;
                color: #1a73e8;
                padding: 12px;
                border-radius: 6px;
                margin-bottom: 16px;
                font-size: 0.95rem;
            }
        </style>
    </head>
    <body>
       
        <div class="sma-card">
            <h1>Portal de Reclamos</h1>
            <p class="mensaje-info">Ingrese sus credenciales para acceder. El sistema valida la PC asignada y el captcha.</p>
            <c:if test="${not empty mensajeError}">
                <div class="mensaje-error">${mensajeError}</div>
            </c:if>
            <form action="${pageContext.request.contextPath}/ControladorLoginSMA" method="post">
                <input type="hidden" name="accion" value="login" />
                <label for="correo">Correo institucional</label>
                <input type="text" id="correo" name="correo" placeholder="usuario@upt.edu.pe" required />

                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password" placeholder="••••••••" required />

                <label>Captcha de seguridad</label>
                <div class="captcha-box">${captcha}</div>
                <input type="text" name="captcha" placeholder="Ingrese el texto mostrado" required />

                <button type="submit">Iniciar sesión</button>
            </form>
        </div>
    </body>
</html>