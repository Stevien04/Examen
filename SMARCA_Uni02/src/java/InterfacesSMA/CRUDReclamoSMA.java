package InterfacesSMA;

import ModeloSMA.ClsCategoriaSMA;
import ModeloSMA.ClsReclamoSMA;
import ModeloSMA.ClsResumenSMA;
import ModeloSMA.ClsSeguimientoSMA;
import java.util.List;
import java.util.Map;

public interface CRUDReclamoSMA {

    List<ClsCategoriaSMA> listarCategorias();

    boolean registrarReclamo(ClsReclamoSMA reclamo);

    List<ClsReclamoSMA> listarReclamosPorUsuario(int idUsuarioSMA);

    List<ClsReclamoSMA> listarReclamos(String estado, Integer idCategoriaSMA);

    ClsReclamoSMA obtenerReclamoPorId(int idReclamoSMA);

    List<ClsSeguimientoSMA> listarSeguimientosPorReclamo(int idReclamoSMA);

    boolean actualizarEstadoReclamo(int idReclamoSMA, String estado, int idAdminSMA, String observacion);

    boolean reclamoPerteneceAUsuario(int idReclamoSMA, int idUsuarioSMA);

    boolean registrarSeguimiento(ClsSeguimientoSMA seguimientoSMA);

    Map<String, Long> obtenerTotalesPorEstado();

    List<ClsResumenSMA> resumenPorCategoriaYEstado();
}