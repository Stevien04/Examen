package ModeloSMA;

import java.io.Serializable;

/**
 * Representa a un usuario del sistema con su información esencial y el rol
 * que determina los permisos de acceso.
 */
public class ClsUsuarioSMA implements Serializable {

    private int idUsuarioSMA;
    private String nombre;
    private String correo;
    private String password;
    private int idRolSMA;
    private String nombreRol;
    private String ipAutorizada;
    private boolean activo;

    public int getIdUsuarioSMA() {
        return idUsuarioSMA;
    }

    public void setIdUsuarioSMA(int idUsuarioSMA) {
        this.idUsuarioSMA = idUsuarioSMA;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdRolSMA() {
        return idRolSMA;
    }

    public void setIdRolSMA(int idRolSMA) {
        this.idRolSMA = idRolSMA;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getIpAutorizada() {
        return ipAutorizada;
    }

    public void setIpAutorizada(String ipAutorizada) {
        this.ipAutorizada = ipAutorizada;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean esAdministrador() {
        return "Administrador".equalsIgnoreCase(nombreRol);
    }
}