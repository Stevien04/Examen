<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
        .resumen-estados { display: flex; gap: 20px; flex-wrap: wrap; }
        .resumen-estados .item { background: #2563eb; color: #fff; padding: 16px; border-radius: 10px; min-width: 160px; flex: 1 1 180px; text-align: center; }
        .resumen-estados .item span { display: block; font-size: 13px; opacity: 0.85; margin-top: 4px; }
        th, td { padding: 12px 14px; border-bottom: 1px solid #e5e7eb; text-align: left; }
        th { background: #f3f4f6; color: #111827; }
        .chart-card { display: grid; gap: 24px; }
        .chart-wrapper { position: relative; width: 100%; max-width: 640px; margin: 0 auto; }
        .chart-legend { display: flex; justify-content: center; gap: 16px; flex-wrap: wrap; font-size: 14px; }
        .chart-legend span { display: inline-flex; align-items: center; gap: 6px; }
        .legend-color { width: 12px; height: 12px; border-radius: 9999px; display: inline-block; }
    </style>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels"></script>
</head>
<body>
    <header>
        <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=panelAdmin">Inicio</a>
        <a href="${pageContext.request.contextPath}/ControladorReclamoSMA?accion=gestion">Gestión de reclamos</a>
        <a href="${pageContext.request.contextPath}/ControladorUsuarioSMA">Usuarios</a>
        <a href="${pageContext.request.contextPath}/ControladorLoginSMA?accion=logout">Salir</a>
    </header>

    <div class="container">
        <c:set var="totalReclamos" value="0" />
        <c:forEach var="entry" items="${resumenEstados}">
            <c:set var="totalReclamos" value="${totalReclamos + entry.value}" />
        </c:forEach>

        <div class="card">
            <h2>Resumen por estado</h2>
            <div class="resumen-estados">
                <c:forEach var="entry" items="${resumenEstados}">
                    <div class="item">
                        <strong>${entry.key}</strong>
                        <div>${entry.value} reclamos</div>
                        <span>
                            <c:choose>
                                <c:when test="${totalReclamos > 0}">
                                    <fmt:formatNumber value="${(entry.value * 100.0) / totalReclamos}" maxFractionDigits="1" />%
                                </c:when>
                                <c:otherwise>0%</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="card chart-card">
            <div>
                <h2>Distribución gráfica de reclamos por estado</h2>
                <div class="chart-wrapper">
                    <canvas id="chartEstados" aria-label="Gráfico de reclamos por estado" role="img"></canvas>
                </div>
                <div class="chart-legend" id="chartEstadosLegend"></div>
            </div>

            <div>
                <h2>Resumen por categoría y estado</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Categoría</th>
                            <th>Estado</th>
                            <th>Total (%)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${resumenCategoria}">
                            <c:set var="porcentaje" value="0" />
                            <c:forEach var="estado" items="${resumenEstados}">
                                <c:if test="${estado.key == item.estado}">
                                    <c:set var="porcentaje" value="${(item.total * 100.0) / totalReclamos}" />
                                </c:if>
                            </c:forEach>
                            <tr>
                                <td>${item.categoria}</td>
                                <td>${item.estado}</td>
                                <td>
                                    ${item.total}
                                    (<fmt:formatNumber value="${porcentaje}" maxFractionDigits="1" />%)
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script>
        const estadosLabels = [];
        const estadosData = [];
        <c:forEach var="entry" items="${resumenEstados}">
            estadosLabels.push("<c:out value='${entry.key}' />".trim() || "Sin estado");
            estadosData.push(${entry.value});
        </c:forEach>

        if (estadosLabels.length > 0 && window.Chart) {
            Chart.register(ChartDataLabels);
            const ctx = document.getElementById('chartEstados').getContext('2d');
            const palette = ['#2563eb','#f97316','#10b981','#facc15','#ec4899','#8b5cf6'];

            const chart = new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels: estadosLabels,
                    datasets: [{
                        data: estadosData,
                        backgroundColor: estadosLabels.map((_, i) => palette[i % palette.length]),
                        borderWidth: 0
                    }]
                },
                options: {
                    plugins: {
                        legend: { display: false },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                    const value = context.parsed;
                                    const percent = total === 0 ? 0 : ((value / total) * 100);
                                    return `${context.label}: ${value} reclamos (${percent.toFixed(1)}%)`;
                                }
                            }
                        },
                        datalabels: {
                            color: '#fff',
                            font: { weight: 'bold' },
                            formatter: (value, ctx) => {
                                const total = ctx.chart._metasets[0].total;
                                const percent = total ? (value / total * 100).toFixed(1) : 0;
                                return percent + '%';
                            }
                        }
                    },
                    cutout: '60%'
                }
            });

            const legendContainer = document.getElementById('chartEstadosLegend');
            chart.data.labels.forEach((label, index) => {
                const value = chart.data.datasets[0].data[index];
                const total = chart.data.datasets[0].data.reduce((sum, item) => sum + item, 0);
                const percent = total === 0 ? 0 : ((value / total) * 100);
                const legendItem = document.createElement('span');
                legendItem.innerHTML = `
                    <span class="legend-color" style="background-color: ${chart.data.datasets[0].backgroundColor[index]};"></span>
                    ${label}: ${value} (${percent.toFixed(1)}%)`;
                legendContainer.appendChild(legendItem);
            });
        }
    </script>
</body>
</html>
