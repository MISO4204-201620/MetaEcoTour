package controllers.implementacion.mensajerias;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.contratos.mensajerias.IComentario;
import controllers.contratos.usuarios.IUsuarios;
import controllers.implementacion.usuarios.Usuarios;
import models.mensajeria.*;
import models.usuario.Usuario;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.libs.Json;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by crimago on 12/03/16.
 */
public class ComentariosController extends Controller {
    private static IComentario comentarios = new Comentarios();
    private static IUsuarios usuarios = new Usuarios();


    @Transactional
    public Result save() {
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"Se ha presentado un problema guardando el comentario.\"}");
        try {


            JsonNode json = request().body().asJson();
            ComentarioDTO comentarioDTO = Json.fromJson(json, ComentarioDTO.class);
            Usuario usuario = usuarios.findByAuthToken(comentarioDTO.getNombreUsuario());
            Comentario comentario = null;
            if (usuario != null){
                comentario = new Comentario();
                comentario.setFecha(comentarioDTO.getFecha());
                comentario.setTexto(comentarioDTO.getComentario());
                comentario.setIdProducto(comentarioDTO.getNumeroComentarios());
                comentario.setOrigen(null);
                comentario.setSubComentarios(null);
                comentario.setIdUsuario(usuario.getId());
                if (comentarioDTO.getOrigen() != 0){
                    comentario.setOrigen(new Comentario(comentarioDTO.getOrigen()));
                }
                if (comentarioDTO.getTipo().equals(Comentario.Tipo.COMENTARIO.toString())){
                    comentario.setTipo(Comentario.Tipo.COMENTARIO);
                } else {
                    comentario.setTipo(Comentario.Tipo.PREGUNTA);
                }


            } else {
                respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"Por favor iniciar sesión para agregar el comentario.\"}");
            }

            if(comentario != null){
                Comentario comentarioHijo = comentarios.save(comentario);
                respuesta = Json.toJson(comentarioHijo);
                //actualizar el padre, con el nuevo hijo
                if (comentarioDTO.getOrigen() != 0) {
                    Comentario comentarioPadre = comentarios.getComentario(comentarioDTO.getOrigen());
                    comentarioPadre.getSubComentarios().add(comentarioHijo);
                    comentarios.save(comentarioPadre);
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return ok(respuesta);
    }


    @Transactional(readOnly=true)
    public Result getComentariosByIdProductoAndType(Long id, int page, String type) {
        Comentario.Tipo tipo = Comentario.Tipo.PREGUNTA;
        if (type.equals(Comentario.Tipo.COMENTARIO.name()))
        {
            tipo = Comentario.Tipo.COMENTARIO;
        }
        return ok(Json.toJson(comentarios.getComentariosByIdProductoAndType(id, page, tipo)));
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
