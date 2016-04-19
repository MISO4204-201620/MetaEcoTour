package controllers.contratos.reportes;

import models.reporte.ComprasDTO;

import java.util.List;

/**
 * Created by JoséLuis on 17/04/2016.
 */
public interface IReporteCompra {

    public List<ComprasDTO> getComprasByProveedorConsolidado(Long idProveedor);
    public List<ComprasDTO> getComprasByProveedorDetalle(Long idProveedor, Long idProducto);

    public List<ComprasDTO> getComprasByUsuarioConsolidado(Long idUsuario);
    public List<ComprasDTO> getComprasByUsuarioDetalle(Long idUsuario, Long idProducto);

}
