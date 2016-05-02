package controllers.implementacion.catalogos;

import com.fasterxml.jackson.databind.JsonNode;
import models.catalogo.Producto;

/**
 * Created by camilo on 1/05/16.
 */
public interface ProductoFactoryMethod {

    public Producto crearProducto(String tipoProducto, JsonNode productoJson);
}
