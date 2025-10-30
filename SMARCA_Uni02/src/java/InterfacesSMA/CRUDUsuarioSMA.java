package InterfacesSMA;

import ModeloSMA.ClsRolSMA;
import ModeloSMA.ClsUsuarioSMA;
import java.util.List;

public interface CRUDUsuarioSMA {

    List<ClsUsuarioSMA> listarUsuarios();

    ClsUsuarioSMA obtenerUsuarioPorId(int idUsuarioSMA);

    boolean registrarUsuario(ClsUsuarioSMA usuario);

    boolean actualizarUsuario(ClsUsuarioSMA usuario);

    boolean eliminarUsuario(int idUsuarioSMA);

    List<ClsRolSMA> listarRoles();
}