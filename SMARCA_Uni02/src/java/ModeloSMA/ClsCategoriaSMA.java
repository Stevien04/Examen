package ModeloSMA;

import java.io.Serializable;

/**
 * Entidad que representa una categoría de reclamo disponible para los
 * usuarios.
 */
public class ClsCategoriaSMA implements Serializable {

    private int idCategoriaSMA;
    private String nombreCategoria;
    private String descripcion;

    public int getIdCategoriaSMA() {
        return idCategoriaSMA;
    }

    public void setIdCategoriaSMA(int idCategoriaSMA) {
        this.idCategoriaSMA = idCategoriaSMA;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}