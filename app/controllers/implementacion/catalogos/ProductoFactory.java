package controllers.implementacion.catalogos;

import com.fasterxml.jackson.databind.JsonNode;
import models.catalogo.Paquete;
import models.catalogo.Producto;
import models.catalogo.Servicio;
import play.libs.Json;

/**
 * Created by camilo on 1/05/16.
 */
public class ProductoFactory implements ProductoFactoryMethod{

    @Override
    public Producto crearProducto(String tipoProducto, JsonNode productoJson) {

        Producto producto = null;

        if("PAQ".equals(tipoProducto)){
            producto= Json.fromJson(productoJson, Paquete.class);

        }else if("SER".equals(tipoProducto)){
            producto = Json.fromJson(productoJson, Servicio.class);
        }
        System.out.println("Saliendo del factory......");
        return producto;
    }
}
