package controllers.implementacion.catalogos;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.contratos.catalogos.IBusqueda;
import models.catalogo.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.db.jpa.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Camilo on 5/03/16.
 */
public class BusquedasController extends Controller {

    IBusqueda busquedas = new Busquedas();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    ObjectMapper objectMapper = new ObjectMapper();

    @Transactional(readOnly = true)
    public Result getBusquedas() {
        return ok(Json.toJson(busquedas.getBusquedas()));
    }

    @Transactional(readOnly = true)
    public Result getBusquedasByProductId(Long productId) {

        return ok(Json.toJson(busquedas.getBusquedasByProductId (productId)));

    }


    @Transactional
    public Result save() {

        JsonNode json = request().body().asJson();
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"El producto de la busqueda no existe\"}");
        Busqueda busquedaProducto = Json.fromJson(json,Busqueda.class);
        if(busquedaProducto!=null){

            busquedaProducto= busquedas.save(busquedaProducto);
            objectMapper.setDateFormat(df);
            Json.setObjectMapper(objectMapper);
            respuesta=Json.toJson(busquedaProducto);
        }
        return ok(Json.toJson(respuesta));
    }


}
