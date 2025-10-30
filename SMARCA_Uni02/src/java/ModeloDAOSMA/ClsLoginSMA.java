package ModeloDAOSMA;

import ConfigSMA.ClsConexionSMA;
import InterfacesSMA.CRUDLoginSMA;
import ModeloSMA.ClsUsuarioSMA;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClsLoginSMA implements CRUDLoginSMA {

    private static final String SQL_VALIDAR = "SELECT u.idUsuarioSMA, u.nombre, u.correo, u.password, u.idRolSMA, u.ip_autorizada, u.activo, r.nombreRol "
            + "FROM usuariosma u INNER JOIN rolsma r ON u.idRolSMA = r.idRolSMA "
            + "WHERE u.correo = ? AND u.password = ? AND u.activo = 1";

    private final ClsConexionSMA conexion;

    public ClsLoginSMA() {
        this.conexion = new ClsConexionSMA();
    }

    @Override
    public ClsUsuarioSMA validarCredenciales(String correo, String password) {
        try (Connection con = conexion.obtenerConexion();
                PreparedStatement ps = con.prepareStatement(SQL_VALIDAR)) {
            ps.setString(1, correo);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
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
        } catch (SQLException e) {
            throw new RuntimeException("Error validando credenciales: " + e.getMessage(), e);
        }
        return null;
    }
}