package ModeloDAOSMA;

import ConfigSMA.ClsConexionSMA;
import InterfacesSMA.CRUDReclamoSMA;
import ModeloSMA.ClsCategoriaSMA;
import ModeloSMA.ClsReclamoSMA;
import ModeloSMA.ClsResumenSMA;
import ModeloSMA.ClsSeguimientoSMA;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClsReclamoDAOSMA implements CRUDReclamoSMA {

    private static final String SQL_LISTAR_CATEGORIAS = "SELECT idCategoriaSMA, nombreCategoria, descripcion FROM categoriasma ORDER BY nombreCategoria";
    private static final String SQL_REGISTRAR_RECLAMO = "INSERT INTO reclamosma (idUsuarioSMA, idCategoriaSMA, descripcion, fecha, estado) VALUES (?, ?, ?, ?, 'Pendiente')";
    private static final String SQL_LISTAR_POR_USUARIO = "SELECT r.idReclamoSMA, r.idUsuarioSMA, r.idCategoriaSMA, r.descripcion, r.fecha, r.estado, c.nombreCategoria "
            + "FROM reclamosma r INNER JOIN categoriasma c ON r.idCategoriaSMA = c.idCategoriaSMA WHERE r.idUsuarioSMA = ? ORDER BY r.fecha DESC, r.idReclamoSMA DESC";
    private static final String SQL_LISTAR_GENERAL = "SELECT r.idReclamoSMA, r.idUsuarioSMA, r.idCategoriaSMA, r.descripcion, r.fecha, r.estado, c.nombreCategoria, u.nombre "
            + "FROM reclamosma r INNER JOIN categoriasma c ON r.idCategoriaSMA = c.idCategoriaSMA "
            + "INNER JOIN usuariosma u ON r.idUsuarioSMA = u.idUsuarioSMA";
    private static final String SQL_OBTENER_RECLAMO = "SELECT r.idReclamoSMA, r.idUsuarioSMA, r.idCategoriaSMA, r.descripcion, r.fecha, r.estado, c.nombreCategoria, u.nombre "
            + "FROM reclamosma r INNER JOIN categoriasma c ON r.idCategoriaSMA = c.idCategoriaSMA "
            + "INNER JOIN usuariosma u ON r.idUsuarioSMA = u.idUsuarioSMA WHERE r.idReclamoSMA = ?";
    private static final String SQL_LISTAR_SEGUIMIENTO = "SELECT s.idSeguimientoSMA, s.idReclamoSMA, s.idAdminSMA, s.fecha, s.observacion, s.estado, u.nombre "
            + "FROM seguimientosma s LEFT JOIN usuariosma u ON s.idAdminSMA = u.idUsuarioSMA WHERE s.idReclamoSMA = ? ORDER BY s.fecha DESC";
    private static final String SQL_ACTUALIZAR_ESTADO = "UPDATE reclamosma SET estado = ? WHERE idReclamoSMA = ?";
    private static final String SQL_REGISTRAR_SEGUIMIENTO = "INSERT INTO seguimientosma (idReclamoSMA, idAdminSMA, fecha, observacion, estado) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_PERTENECE_USUARIO = "SELECT COUNT(1) FROM reclamosma WHERE idReclamoSMA = ? AND idUsuarioSMA = ?";
    private static final String SQL_TOTALES_ESTADO = "SELECT estado, COUNT(*) AS total FROM reclamosma GROUP BY estado";
    private static final String SQL_RESUMEN_CATEGORIA_ESTADO = "SELECT categoria, estado, total FROM vista_resumensma";

    private final ClsConexionSMA conexion;

    public ClsReclamoDAOSMA() {
        this.conexion = new ClsConexionSMA();
    }

    @Override
    public List<ClsCategoriaSMA> listarCategorias() {
        List<ClsCategoriaSMA> categorias = new ArrayList<>();
        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR_CATEGORIAS);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClsCategoriaSMA categoria = new ClsCategoriaSMA();
                categoria.setIdCategoriaSMA(rs.getInt("idCategoriaSMA"));
                categoria.setNombreCategoria(rs.getString("nombreCategoria"));
                categoria.setDescripcion(rs.getString("descripcion"));
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando categorías: " + e.getMessage(), e);
        }
        return categorias;
    }

    @Override
    public boolean registrarReclamo(ClsReclamoSMA reclamo) {
        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(SQL_REGISTRAR_RECLAMO)) {
            ps.setInt(1, reclamo.getIdUsuarioSMA());
            ps.setInt(2, reclamo.getIdCategoriaSMA());
            ps.setString(3, reclamo.getDescripcion());
            LocalDate fecha = reclamo.getFecha() != null ? reclamo.getFecha() : LocalDate.now();
            ps.setDate(4, Date.valueOf(fecha));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error registrando reclamo: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ClsReclamoSMA> listarReclamosPorUsuario(int idUsuarioSMA) {
        List<ClsReclamoSMA> reclamos = new ArrayList<>();
        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR_POR_USUARIO)) {
            ps.setInt(1, idUsuarioSMA);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reclamos.add(mapearReclamo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando reclamos por usuario: " + e.getMessage(), e);
        }
        return reclamos;
    }

    @Override
    public List<ClsReclamoSMA> listarReclamos(String estado, Integer idCategoriaSMA) {
        List<ClsReclamoSMA> reclamos = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SQL_LISTAR_GENERAL);
        List<Object> parametros = new ArrayList<>();

        boolean tieneWhere = false;
        if (estado != null && !estado.isBlank()) {
            sql.append(" WHERE r.estado = ?");
            parametros.add(estado);
            tieneWhere = true;
        }
        if (idCategoriaSMA != null) {
            sql.append(tieneWhere ? " AND" : " WHERE").append(" r.idCategoriaSMA = ?");
            parametros.add(idCategoriaSMA);
        }
        sql.append(" ORDER BY r.estado, r.fecha DESC, r.idReclamoSMA DESC");

        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ClsReclamoSMA reclamo = mapearReclamo(rs);
                    reclamo.setNombreUsuario(rs.getString("nombre"));
                    reclamos.add(reclamo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando reclamos: " + e.getMessage(), e);
        }
        return reclamos;
    }

    @Override
    public ClsReclamoSMA obtenerReclamoPorId(int idReclamoSMA) {
        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(SQL_OBTENER_RECLAMO)) {
            ps.setInt(1, idReclamoSMA);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ClsReclamoSMA reclamo = mapearReclamo(rs);
                    reclamo.setNombreUsuario(rs.getString("nombre"));
                    return reclamo;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo reclamo: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<ClsSeguimientoSMA> listarSeguimientosPorReclamo(int idReclamoSMA) {
        List<ClsSeguimientoSMA> seguimientos = new ArrayList<>();
        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR_SEGUIMIENTO)) {
            ps.setInt(1, idReclamoSMA);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ClsSeguimientoSMA seguimiento = new ClsSeguimientoSMA();
                    seguimiento.setIdSeguimientoSMA(rs.getInt("idSeguimientoSMA"));
                    seguimiento.setIdReclamoSMA(rs.getInt("idReclamoSMA"));
                    int idAdmin = rs.getInt("idAdminSMA");
                    seguimiento.setIdAdminSMA(rs.wasNull() ? null : idAdmin);
                    Timestamp ts = rs.getTimestamp("fecha");
                    if (ts != null) {
                        seguimiento.setFecha(ts.toLocalDateTime());
                    }
                    seguimiento.setObservacion(rs.getString("observacion"));
                    seguimiento.setEstado(rs.getString("estado"));
                    seguimiento.setNombreAdmin(rs.getString("nombre"));
                    seguimientos.add(seguimiento);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando seguimiento: " + e.getMessage(), e);
        }
        return seguimientos;
    }

    @Override
    public boolean actualizarEstadoReclamo(int idReclamoSMA, String estado, int idAdminSMA, String observacion) {
        try (Connection con = conexion.obtenerConexion()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR_ESTADO)) {
                    ps.setString(1, estado);
                    ps.setInt(2, idReclamoSMA);
                    ps.executeUpdate();
                }

                registrarSeguimientoInterno(con, idReclamoSMA, idAdminSMA, estado, observacion);

                con.commit();
                return true;
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando estado del reclamo: " + e.getMessage(), e);
        }
    }

    private void registrarSeguimientoInterno(Connection con, int idReclamoSMA, int idAdminSMA, String estado, String observacion) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SQL_REGISTRAR_SEGUIMIENTO)) {
            ps.setInt(1, idReclamoSMA);
            ps.setInt(2, idAdminSMA);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(4, observacion);
            ps.setString(5, estado);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean reclamoPerteneceAUsuario(int idReclamoSMA, int idUsuarioSMA) {
        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(SQL_PERTENECE_USUARIO)) {
            ps.setInt(1, idReclamoSMA);
            ps.setInt(2, idUsuarioSMA);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error validando pertenencia del reclamo: " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean registrarSeguimiento(ClsSeguimientoSMA seguimientoSMA) {
        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(SQL_REGISTRAR_SEGUIMIENTO)) {
            ps.setInt(1, seguimientoSMA.getIdReclamoSMA());
            if (seguimientoSMA.getIdAdminSMA() == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, seguimientoSMA.getIdAdminSMA());
            }
            ps.setTimestamp(3, Timestamp.valueOf(seguimientoSMA.getFecha() != null ? seguimientoSMA.getFecha() : LocalDateTime.now()));
            ps.setString(4, seguimientoSMA.getObservacion());
            ps.setString(5, seguimientoSMA.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error registrando seguimiento: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Long> obtenerTotalesPorEstado() {
        Map<String, Long> totales = new LinkedHashMap<>();
        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(SQL_TOTALES_ESTADO);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                totales.put(rs.getString("estado"), rs.getLong("total"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo totales por estado: " + e.getMessage(), e);
        }
        return totales;
    }

    @Override
    public List<ClsResumenSMA> resumenPorCategoriaYEstado() {
        List<ClsResumenSMA> resumen = new ArrayList<>();
        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(SQL_RESUMEN_CATEGORIA_ESTADO);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClsResumenSMA item = new ClsResumenSMA();
                item.setCategoria(rs.getString("categoria"));
                item.setEstado(rs.getString("estado"));
                item.setTotal(rs.getLong("total"));
                resumen.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo resumen por categoría: " + e.getMessage(), e);
        }
        return resumen;
    }

    private ClsReclamoSMA mapearReclamo(ResultSet rs) throws SQLException {
        ClsReclamoSMA reclamo = new ClsReclamoSMA();
        reclamo.setIdReclamoSMA(rs.getInt("idReclamoSMA"));
        reclamo.setIdUsuarioSMA(rs.getInt("idUsuarioSMA"));
        reclamo.setIdCategoriaSMA(rs.getInt("idCategoriaSMA"));
        reclamo.setDescripcion(rs.getString("descripcion"));
        Date fecha = rs.getDate("fecha");
        if (fecha != null) {
            reclamo.setFecha(fecha.toLocalDate());
        } else {
            reclamo.setFecha(LocalDate.now());
        }
        reclamo.setEstado(rs.getString("estado"));
        reclamo.setNombreCategoria(rs.getString("nombreCategoria"));
        return reclamo;
    }

}