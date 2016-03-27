package controllers.implementacion.compras;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.contratos.compras.ITransaccion;
import models.compra.Transaccion;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by JoséLuis on 26/03/2016.
 */
public class TransaccionesController extends Controller {
    private static ITransaccion transacciones = new Transacciones();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public Result pagarCompra() {

        JsonNode json = request().body().asJson();
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"El producto de la calificación no existe\"}");
        Transaccion trans = Json.fromJson(json,Transaccion.class);
        if(trans!=null){
            trans = transacciones.pagarCompra(trans);
            objectMapper.setDateFormat(df);
            Json.setObjectMapper(objectMapper);
            respuesta=Json.toJson(trans);
        }
        return ok(respuesta);
    }


}
