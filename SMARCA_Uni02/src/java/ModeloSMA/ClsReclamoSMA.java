package ModeloSMA;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Modelo que encapsula la información de un reclamo registrado por los
 * usuarios o gestionado por los administradores.
 */
public class ClsReclamoSMA implements Serializable {

    private int idReclamoSMA;
    private int idUsuarioSMA;
    private int idCategoriaSMA;
    private String descripcion;
    private LocalDate fecha;
    private String estado;
    private String nombreCategoria;
    private String nombreUsuario;

    public int getIdReclamoSMA() {
        return idReclamoSMA;
    }

    public void setIdReclamoSMA(int idReclamoSMA) {
        this.idReclamoSMA = idReclamoSMA;
    }

    public int getIdUsuarioSMA() {
        return idUsuarioSMA;
    }

    public void setIdUsuarioSMA(int idUsuarioSMA) {
        this.idUsuarioSMA = idUsuarioSMA;
    }

    public int getIdCategoriaSMA() {
        return idCategoriaSMA;
    }

    public void setIdCategoriaSMA(int idCategoriaSMA) {
        this.idCategoriaSMA = idCategoriaSMA;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}