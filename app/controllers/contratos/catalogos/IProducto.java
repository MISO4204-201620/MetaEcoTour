package controllers.contratos.catalogos;

import models.catalogo.Atributo;
import models.catalogo.Categoria;
import models.catalogo.Producto;

import java.util.List;

/**
 * Created by Camilo on 27/02/16.
 */
public interface IProducto {

    public List<Producto> getProductos();

    public List<Producto> getProductosByType(String productType);

    public List<Producto> getProductosByPageByType(Integer numPage,String productType);

    public List<Producto> getProductosByPageByTypeAndCategory(Integer numPage,String productType, long idCategory);

    public List<Producto> getProductosByPageByFilters(Integer numPage,String name,Double precioInicial, Double precioFinal, String productType, Long idProvider, Long idCategory);

    public List<Producto> getProductsByProveedorId(Long idProveedor);

    public List<Atributo> getAttributeByProductId(Long idProducto);

    public Atributo addAtributo(Atributo atributo);

    public Atributo removeAtributo(Atributo atributo);

    Producto getProductById(Long id);

    Producto save(Producto producto);

    Producto delete(Long id);
}