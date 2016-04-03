package controllers.implementacion.compras;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.contratos.compras.ICalificacion;
import models.compra.Calificacion;
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

    @Transactional
    public Result getCalificaionesByServicio(Long idServicio) {
        return ok(Json.toJson(calificaciones.getCalificacionByServicio(idServicio)));
    }

    @Transactional
    public Result getCalificaionesByUsuario(Long idUsuario) {
        return ok(Json.toJson(calificaciones.getCalificacionByUsuario(idUsuario)));
    }

    @Transactional
    public Result getPromedioByServicio(Long idServicio) {
        return ok(Json.toJson(calificaciones.getPromedioByServicio(idServicio)));
    }


    @Transactional
    public Result save() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode json = request().body().asJson();
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"El producto de la calificación no existe\"}");
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
        return ok(Json.toJson(respuesta));
    }



}
