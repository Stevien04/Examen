package ModeloSMA;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Representa un registro de seguimiento asociado a un reclamo.
 */
public class ClsSeguimientoSMA implements Serializable {

    private int idSeguimientoSMA;
    private int idReclamoSMA;
    private Integer idAdminSMA;
    private LocalDateTime fecha;
    private String observacion;
    private String estado;
    private String nombreAdmin;

    public int getIdSeguimientoSMA() {
        return idSeguimientoSMA;
    }

    public void setIdSeguimientoSMA(int idSeguimientoSMA) {
        this.idSeguimientoSMA = idSeguimientoSMA;
    }

    public int getIdReclamoSMA() {
        return idReclamoSMA;
    }

    public void setIdReclamoSMA(int idReclamoSMA) {
        this.idReclamoSMA = idReclamoSMA;
    }

    public Integer getIdAdminSMA() {
        return idAdminSMA;
    }

    public void setIdAdminSMA(Integer idAdminSMA) {
        this.idAdminSMA = idAdminSMA;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreAdmin() {
        return nombreAdmin;
    }

    public void setNombreAdmin(String nombreAdmin) {
        this.nombreAdmin = nombreAdmin;
    }
}