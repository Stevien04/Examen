package ModeloDAOSMA;

import ConfigSMA.ClsConexionSMA;
import InterfacesSMA.CRUDUsuarioSMA;
import ModeloSMA.ClsRolSMA;
import ModeloSMA.ClsUsuarioSMA;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClsUsuarioDAOSMA implements CRUDUsuarioSMA {

    private static final String SQL_LISTAR = "SELECT u.idUsuarioSMA, u.nombre, u.correo, u.password, u.idRolSMA, u.ip_autorizada, u.activo, r.nombreRol "
            + "FROM usuariosma u INNER JOIN rolsma r ON u.idRolSMA = r.idRolSMA ORDER BY u.nombre";
    private static final String SQL_OBTENER = "SELECT u.idUsuarioSMA, u.nombre, u.correo, u.password, u.idRolSMA, u.ip_autorizada, u.activo, r.nombreRol "
            + "FROM usuariosma u INNER JOIN rolsma r ON u.idRolSMA = r.idRolSMA WHERE u.idUsuarioSMA = ?";
    private static final String SQL_INSERTAR = "INSERT INTO usuariosma (nombre, correo, password, idRolSMA, ip_autorizada, activo) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_ACTUALIZAR = "UPDATE usuariosma SET nombre = ?, correo = ?, password = ?, idRolSMA = ?, ip_autorizada = ?, activo = ? WHERE idUsuarioSMA = ?";
    private static final String SQL_ELIMINAR = "UPDATE usuariosma SET activo = 0 WHERE idUsuarioSMA = ?";
    private static final String SQL_LISTAR_ROLES = "SELECT idRolSMA, nombreRol FROM rolsma ORDER BY nombreRol";

    private final ClsConexionSMA conexion;

    public ClsUsuarioDAOSMA() {
        this.conexion = new ClsConexionSMA();
    }

    @Override
    public List<ClsUsuarioSMA> listarUsuarios() {
        List<ClsUsuarioSMA> usuarios = new ArrayList<>();
        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando usuarios: " + e.getMessage(), e);
        }
        return usuarios;
    }

    @Override
    public ClsUsuarioSMA obtenerUsuarioPorId(int idUsuarioSMA) {
        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(SQL_OBTENER)) {
            ps.setInt(1, idUsuarioSMA);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo usuario: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean registrarUsuario(ClsUsuarioSMA usuario) {
        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getPassword());
            ps.setInt(4, usuario.getIdRolSMA());
            ps.setString(5, usuario.getIpAutorizada());
            ps.setBoolean(6, usuario.isActivo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error registrando usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizarUsuario(ClsUsuarioSMA usuario) {
        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getPassword());
            ps.setInt(4, usuario.getIdRolSMA());
            ps.setString(5, usuario.getIpAutorizada());
            ps.setBoolean(6, usuario.isActivo());
            ps.setInt(7, usuario.getIdUsuarioSMA());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminarUsuario(int idUsuarioSMA) {
        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {
            ps.setInt(1, idUsuarioSMA);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ClsRolSMA> listarRoles() {
        List<ClsRolSMA> roles = new ArrayList<>();
        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR_ROLES);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClsRolSMA rol = new ClsRolSMA();
                rol.setIdRolSMA(rs.getInt("idRolSMA"));
                rol.setNombreRol(rs.getString("nombreRol"));
                roles.add(rol);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando roles: " + e.getMessage(), e);
        }
        return roles;
    }

    private ClsUsuarioSMA mapearUsuario(ResultSet rs) throws SQLException {
        ClsUsuarioSMA usuario = new ClsUsuarioSMA();
        usuario.setIdUsuarioSMA(rs.getInt("idUsuarioSMA"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setPassword(rs.getString("password"));
        usuario.setIdRolSMA(rs.getInt("idRolSMA"));
        usuario.setIpAutorizada(rs.getString("ip_autorizada"));
        usuario.setActivo(rs.getBoolean("activo"));
        usuario.setNombreRol(rs.getString("nombreRol"));
        return usuario;
    }
}