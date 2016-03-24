package controllers.implementacion.catalogos;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.contratos.catalogos.IProducto;
import controllers.contratos.catalogos.IRecurso;
import models.catalogo.Categoria;
import models.catalogo.Recurso;
import models.catalogo.Producto;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.db.jpa.Transactional;

import views.html.detail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JoséLuis on 27/02/2016.
 */
public class RecursosController extends Controller {
    private static IRecurso recursos = new Recursos();
    private static IProducto productos = new Productos();


    @Transactional
    public Result save() {
        JsonNode json = request().body().asJson();
        List<Recurso> recursosGuardados = new ArrayList<Recurso>();
        Long idProducto = json.get("idProducto").asLong();
        for (JsonNode recursoFromArray : json.withArray("recursos")) {
            Recurso recurso = Json.fromJson(recursoFromArray, Recurso.class);

            if (recurso != null) {
                recurso.setIdProducto(idProducto);

                recurso = recursos.save(recurso);
                recursosGuardados.add(recurso);
            }
        }
        return ok(Json.toJson(recursosGuardados));
    }

    @Transactional
    public Result delete(String id) {
        return ok(Json.toJson(recursos.delete(Long.parseLong(id))));
    }

    @Transactional(readOnly=true)
    public Result getRecursosByProd(Long prodId) {
        return ok(Json.toJson(recursos.getRecursosByProd(prodId)));
        //return ok(detail.render("Catálogo MetaEcoTour"));
        //return ok(detail.render(String.valueOf(Json.toJson(recursos.getRecursosByProd(prodId)))));
        //List<Recurso> recurso = recursos.getRecursosByProd(prodId);
        //return ok(detail.render(recurso, productos.getProductById(prodId)));
        //return ok(detail.render(productos.getProductById(prodId)));
    }

    @Transactional(readOnly=true)
    public Result getRecursosByProdByType(Long prodId, String recTypeType) {
        return ok(Json.toJson(recursos.getRecursosByProdByType(prodId,recTypeType)));
    }
}
