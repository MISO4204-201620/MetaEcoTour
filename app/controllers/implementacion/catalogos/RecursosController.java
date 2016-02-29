package controllers.implementacion.catalogos;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.contratos.catalogos.IRecurso;
import models.catalogo.Categoria;
import models.catalogo.Recurso;
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
        JsonNode json = request().body().asJson();
        Recurso recurso = Json.fromJson(json, Recurso.class);
        if (recurso != null){
            recursos.save(recurso);
        }
        return ok();
    }

    @Transactional
    public Result delete(String id) {
        return ok(Json.toJson(recursos.delete(Long.parseLong(id))));
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
