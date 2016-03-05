package controllers.contratos.catalogos;

import models.catalogo.Busqueda;


import java.util.List;

/**
 * Created by Camilo on 5/03/16.
 */
public interface IBusqueda {


    public List<Busqueda> getBusquedas();

    public List<Busqueda> getBusquedasByProductId(Long productId);

    public Busqueda save(Busqueda busqueda);

    public Busqueda delete(Long l);

    public void deleteAllSearchByProdId(Long id);

}
