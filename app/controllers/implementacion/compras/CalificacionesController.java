package controllers.implementacion.compras;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.contratos.compras.ICalificacion;
import models.compra.Calificacion;
import play.Configuration;
import play.Play;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by JoséLuis on 13/03/2016.
 */
public class CalificacionesController extends Controller {
    private static ICalificacion calificaciones = new Calificaciones();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    ObjectMapper objectMapper = new ObjectMapper();

    Configuration conf = Play.application().configuration();
    boolean calificacionComentar= conf.getBoolean("calificacion.comentar");
    boolean calificacionConsultar= conf.getBoolean("calificacion.consultar");


    @Transactional
    public Result getCalificaionesByServicio(Long idServicio) {
        JsonNode respuesta = Json.parse("{\"errorCode\":\"2\",\"desCode\":\"El servicio no se encuentra activo\"}");
        if (calificacionConsultar){
            return ok(Json.toJson(calificaciones.getCalificacionByServicio(idServicio)));
        }else {
            return ok(Json.toJson(respuesta));
        }

    }

    @Transactional
    public Result getCalificaionesByUsuario(Long idUsuario) {
        JsonNode respuesta = Json.parse("{\"errorCode\":\"2\",\"desCode\":\"El servicio no se encuentra activo\"}");
        if (calificacionConsultar){
            return ok(Json.toJson(calificaciones.getCalificacionByUsuario(idUsuario)));
        }else {
            return ok(Json.toJson(respuesta));
        }
    }

    @Transactional
    public Result getPromedioByServicio(Long idServicio) {
        return ok(Json.toJson(calificaciones.getPromedioByServicio(idServicio)));
    }


    @Transactional
    public Result save() {

        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"El producto de la calificación no existe\"}");
        if (calificacionComentar) {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode json = request().body().asJson();

            Calificacion calificaProducto = Json.fromJson(json, Calificacion.class);
            if(calificaProducto != null){
                if(!calificaciones.validaCompraProducto(calificaProducto.getIdUsuario(),calificaProducto.getIdProducto())){
                    respuesta = Json.parse("{\"errorCode\":\"2\",\"desCode\":\"El usuario no ha comprado el producto.\"}");
                }else{
                    calificaProducto= calificaciones.save(calificaProducto);
                    objectMapper.setDateFormat(df);
                    Json.setObjectMapper(objectMapper);
                    respuesta = Json.toJson(calificaProducto);
                }
            }

        } else {
            respuesta = Json.parse("{\"errorCode\":\"2\",\"desCode\":\"El servicio no se encuentra activo\"}");
        }
        return ok(Json.toJson(respuesta));
    }



}
