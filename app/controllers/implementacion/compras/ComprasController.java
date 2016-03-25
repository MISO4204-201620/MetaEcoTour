package controllers.implementacion.compras;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.contratos.compras.ICompra;
import models.compra.Compra;
import models.compra.ItemCompra;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by JoséLuis on 19/03/2016.
 */
public class ComprasController extends Controller {
    private static ICompra compras = new Compras();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public Result getCompra(Long idCompra) {
        return ok(Json.toJson(compras.getCompra(idCompra)));
    }

    @Transactional
    public Result getCompraActivaByUsuario(Long idUsuario) {
        return ok(Json.toJson(compras.getCompraActivaByUsuario(idUsuario)));
    }

    @Transactional
    public Result getItemsCompra(Long idCompra){
        return ok(Json.toJson(compras.getItemmsCompra(idCompra)));
    }

    @Transactional
    public Result save() {

        JsonNode json = request().body().asJson();
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"El producto de la calificación no existe\"}");
        System.out.println("Antes de la serializacion");
        Compra compra = Json.fromJson(json,Compra.class);
        System.out.println("Despues de la serializacion");
        if(compra!=null){
            System.out.println(compra.getFechaCreacion());
            compra= compras.save(compra);
            objectMapper.setDateFormat(df);
            Json.setObjectMapper(objectMapper);
            System.out.println("Antes del json to json");
            respuesta=Json.toJson(compra);
        }
        return ok(respuesta);
    }

    @Transactional
    public Result addItemsCompra() {

        JsonNode json = request().body().asJson();
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"El producto de la calificación no existe\"}");
        ItemCompra itmCompra = Json.fromJson(json,ItemCompra.class);
        if(itmCompra!=null){

            itmCompra= compras.addItemmsCompra(itmCompra);
            objectMapper.setDateFormat(df);
            Json.setObjectMapper(objectMapper);
            respuesta=Json.toJson(itmCompra);
        }
        return ok(Json.toJson(respuesta));
    }

    @Transactional
    public Result removeItemsCompra() {

        JsonNode json = request().body().asJson();
        JsonNode respuesta = Json.parse("{\"errorCode\":\"1\",\"desCode\":\"El producto de la calificación no existe\"}");
        ItemCompra itmCompra = Json.fromJson(json,ItemCompra.class);
        if(itmCompra!=null){

            itmCompra= compras.removeItemmsCompra(itmCompra);
            objectMapper.setDateFormat(df);
            Json.setObjectMapper(objectMapper);
            respuesta=Json.toJson(itmCompra);
        }
        return ok(Json.toJson(respuesta));
    }

}
