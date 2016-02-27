package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IRecurso;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.db.jpa.Transactional;

/**
 * Created by JoséLuis on 27/02/2016.
 */
public class RecursosController extends Controller {
    private static IRecurso recursos = new Recursos();


    @Transactional
    public Result save() {
        return null;
    }

    @Transactional
    public Result delete(String id) {
        return null;
    }

    @Transactional(readOnly=true)
    public Result getRecursosByProd(Long prodId) {
        return ok(Json.toJson(recursos.getRecursosByProd(prodId)));
    }

    @Transactional(readOnly=true)
    public Result getRecursosByProdByType(Long prodId, String recTypeType) {
        return ok(Json.toJson(recursos.getRecursosByProdByType(prodId,recTypeType)));
    }
}
