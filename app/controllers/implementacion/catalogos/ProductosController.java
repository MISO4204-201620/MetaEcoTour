package controllers.implementacion.catalogos;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.contratos.catalogos.IProducto;
import models.catalogo.*;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Camilo on 27/02/16.
 */
public class ProductosController extends Controller {
    private static IProducto productos = new Productos();


    @Transactional
    public Result save() {
        JsonNode json = request().body().asJson();

        String tipoProducto=json.get("tipoProducto").textValue();

        JsonNode productoJson =json.get("producto");
        JsonNode recursos =json.get("producto").get("recursos");

        Producto producto = null;
        if("PAQ".equals(tipoProducto)){
            producto = Json.fromJson(productoJson, Paquete.class);
        }else if("SER".equals(tipoProducto)){
            producto = Json.fromJson(productoJson, Servicio.class);
        }

        if(producto != null){
           producto=productos.save(producto);
        }

        List<Recurso> recursosUpdate=producto.getRecursos();
        for(int i=0;i<recursos.size();i++){
            if(recursosUpdate==null){
                recursosUpdate=new ArrayList<Recurso>();
            }
            Recurso recursoTmp = Json.fromJson(recursos.get(i),Recurso.class);
            recursoTmp.setIdProducto(producto.getId());
            recursosUpdate.add(recursoTmp);
        }
        System.out.println(recursosUpdate);
        producto.setRecursos(recursosUpdate);
        return ok(Json.toJson(producto));

    }

    @Transactional
    public Result delete(String id) {
        return null;
    }

    @Transactional(readOnly=true)
    public Result getProductos() {
        return ok(Json.toJson(productos.getProductos()));
    }

    @Transactional(readOnly=true)
    public Result getProductosByType(String productType) {
        return ok(Json.toJson(productos.getProductosByType(productType)));
    }

    @Transactional(readOnly=true)
    public Result getProductById(Long productId) {
        return ok(Json.toJson(productos.getProductById(productId)));
    }
}
