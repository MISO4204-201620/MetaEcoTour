package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IProducto;
import models.catalogo.Producto;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by Camilo on 27/02/16.
 */
public class ProductosController extends Controller {
    private static IProducto productos = new Productos();


    @Transactional
    public Result save() {
        return null;
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

}
