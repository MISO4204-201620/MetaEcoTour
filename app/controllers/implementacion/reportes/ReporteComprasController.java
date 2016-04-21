package controllers.implementacion.reportes;

import controllers.contratos.reportes.IReporteCompra;
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
    public Result getComprasConsolidado(String entidad, Long id) {
        int vista=this.getVista(entidad);
        return ok(Json.toJson(reportes.getComprasConsolidado(vista,id)));
    }

    @Transactional
    public Result getComprasDetalle(String entidad, Long id, Long idProducto) {
        int vista=this.getVista(entidad);
        return ok(Json.toJson(reportes.getComprasDetalle(vista,id,idProducto)));
    }

private int getVista(String entidad ){
    int vista=0;
    switch(entidad){
        case "prv":{
            vista=1;
        }break;
        case "usr":{
            vista=2;
        }break;
    }
return vista;

}


}
