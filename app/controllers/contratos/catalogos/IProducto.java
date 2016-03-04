package controllers.contratos.catalogos;

import models.catalogo.Categoria;
import models.catalogo.Producto;

import java.util.List;

/**
 * Created by Camilo on 27/02/16.
 */
public interface IProducto {

    public List<Producto> getProductos();

    public List<Producto> getProductosByType(String productType);

    Producto getProductById(Long id);

    Producto save(Producto producto);

    Producto delete(Long id);
}
