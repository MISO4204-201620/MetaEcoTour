package controllers.implementacion.reportes;

import controllers.contratos.reportes.IReporte;
import controllers.contratos.reportes.IReporteCompra;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;


/**
 * Created by JoséLuis on 14/04/2016.
 */
public class ReporteComprasController extends Controller {
private static IReporteCompra reportes = new ReporteCompras();

    @Transactional
    public Result getComprasByProveedorConsolidado(Long idProveedor) {
        return ok(Json.toJson(reportes.getComprasByProveedorConsolidado(idProveedor)));
    }

    @Transactional
    public Result getComprasByProveedorDetalle(Long idProveedor, Long idProducto) {
        return ok(Json.toJson(reportes.getComprasByProveedorDetalle(idProveedor,idProducto)));
    }

    @Transactional
    public Result getComprasByUsuarioConsolidado(Long idUsuario) {
        return ok(Json.toJson(reportes.getComprasByUsuarioConsolidado(idUsuario)));
    }

    @Transactional
    public Result getComprasByUsuarioDetalle(Long idUsuario, Long idProducto) {
        return ok(Json.toJson(reportes.getComprasByUsuarioDetalle(idUsuario,idProducto)));
    }

}
