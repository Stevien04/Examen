package ModeloSMA;

import java.io.Serializable;

/**
 * Modelo para las definiciones de roles disponibles en el sistema.
 */
public class ClsRolSMA implements Serializable {

    private int idRolSMA;
    private String nombreRol;

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
}