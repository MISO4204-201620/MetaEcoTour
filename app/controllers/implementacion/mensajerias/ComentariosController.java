package controllers.implementacion.mensajerias;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.contratos.mensajerias.IComentario;
import models.mensajeria.Comentario;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.libs.Json;

import java.util.List;

/**
 * Created by crimago on 12/03/16.
 */
public class ComentariosController extends Controller {
    private static IComentario comentarios = new Comentarios();



    @Transactional
    public Result save() {
        JsonNode json = request().body().asJson();

        Comentario comentario = Json.fromJson(json, Comentario.class);
        if(comentario != null){
            comentarios.save(comentario);
        }
        return ok();

    }


    @Transactional(readOnly=true)
    public Result getComentariosByIdProducto(Long id, int page) {
        return ok(Json.toJson(comentarios.getComentariosByIdProducto(id, page)));
    }

    @Transactional(readOnly=true)
    public Result getComentariosByPadre(Long id) {
        return ok(Json.toJson(comentarios.getComentariosByIdPadreComentario(id)));
    }

    @Transactional(readOnly=true)
    public Result getComentariosByIdUsuario(Long id) {
        return ok(Json.toJson(comentarios.getComentariosByIdUsuario(id)));
    }

    @Transactional(readOnly=true)
    public Result getSubComentarios(Long id) {
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"No existen subcomentarios\"}");
        List<Comentario> subcomentarios = comentarios.getSubComentarios(id);
        if (subcomentarios != null){
            respuesta = Json.toJson(subcomentarios);
        }
        return ok(respuesta);
    }

    @Transactional
    public Result delete(Long id) {
        Comentario comentario = comentarios.delete(id);
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"El producto no existe\"}");
        if(comentario!=null){
            respuesta= Json.toJson(comentario);
        }
        return ok(respuesta);
    }
}
