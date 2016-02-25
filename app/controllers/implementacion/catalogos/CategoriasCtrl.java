package controllers.implementacion.catalogos;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.contratos.catalogos.ICategorias;
import models.catalogo.Categoria;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by jose on 2/20/16.
 */
public class CategoriasCtrl extends Controller {
    private static ICategorias categorias = new Categorias();

    @Transactional
    public Result delete(String id) {
        System.out.println("Se quiere eliminar la categoría con id: " + id);
        return ok(Json.toJson(categorias.delete(Long.parseLong(id))));

    }

    @Transactional
    public Result save() {
        JsonNode json = request().body().asJson();
        Categoria categoria = Json.fromJson(json, Categoria.class);
        if(categoria != null){
            categorias.save(categoria);
        }else{
            System.out.println("CATEGORIA IS NULL");
        }

        return ok();
    }

    @Transactional(readOnly=true)
    public Result getCategorias(){
        return ok(Json.toJson(categorias.getCategorias()));
    }
}
