package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IPaquete;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by JoséLuis on 05/03/2016.
 */
public class PaquetesController extends Controller {
    private static IPaquete paquetes = new Paquetes();

    @Transactional(readOnly=true)
    public Result getItemServicios(Long idPaquete) {
        return ok(Json.toJson(paquetes.getItemServicios(idPaquete)));
    }

    @Transactional(readOnly=true)
    public Result getServicios(Long idPaquete) {
        return ok(Json.toJson(paquetes.getServicios(idPaquete)));
    }

    @Transactional(readOnly=true)
    public Result addServicioToPaquete(Long isPaquete, Long idServ, int cantidad) {
        return ok(Json.toJson(paquetes.addServicioToPaquete(isPaquete,  idServ,  cantidad)));
    }

    @Transactional(readOnly=true)
    public Result removeServicioFromPaquete(Long isPaquete, Long idServ) {
        return ok(Json.toJson(paquetes.removeServicioFromPaquete( isPaquete,  idServ)));
    }

}
