package controllers.contratos.catalogos;

import models.catalogo.Busqueda;


import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Camilo on 5/03/16.
 */
public interface IBusqueda {


    public List<Busqueda> getBusquedas();

    public List<Busqueda> getBusquedasByProductId(Long productId);

    public List<Busqueda> getBusquedasByTypeAndDate(String type, Date fechaInicio, Date fechaFin);

    public HashMap<String,String> getBusquedasByTypeDateAndCount(String tipo, Date fechaInicio, Date fechaFin);

    public Busqueda save(Busqueda busqueda);

    public Busqueda delete(Long l);

    public void deleteAllSearchByProdId(Long id);

}
