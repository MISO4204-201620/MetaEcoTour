package controllers.contratos.reportes;

import models.reporte.ComprasDTO;

import java.util.List;

/**
 * Created by JoséLuis on 17/04/2016.
 */
public interface IReporteCompra {

    public List<ComprasDTO> getComprasConsolidado(int vista, Long id);
    public List<ComprasDTO> getComprasDetalle(int vista, Long id, Long idProducto);

}
