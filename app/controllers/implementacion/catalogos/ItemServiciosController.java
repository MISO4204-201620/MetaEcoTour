package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IItemServicio;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.db.jpa.Transactional;

/**
 * Created by JoséLuis on 27/02/2016.
 */
public class ItemServiciosController  extends Controller {
    private static IItemServicio itemServicios = new ItemServicios();


    @Transactional
    public Result save() {
        return null;
    }

    @Transactional
    public Result delete(String id) {
        return null;
    }

    @Transactional(readOnly=true)
    public Result getItemsByPaquetes(Long idPaquete) {
        return ok(Json.toJson(itemServicios.getItemsByPaquetes(idPaquete)));
    }

    @Transactional(readOnly=true)
    public Result getItemsByServicios(Long idServicio) {
        return ok(Json.toJson(itemServicios.getItemsByServicios(idServicio)));
    }
}
