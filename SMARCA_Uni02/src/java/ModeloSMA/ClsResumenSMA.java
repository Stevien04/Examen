package ModeloSMA;

import java.io.Serializable;

/**
 * Representa un resumen de reclamos agrupados por categoría y estado.
 */
public class ClsResumenSMA implements Serializable {

    private String categoria;
    private String estado;
    private long total;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}