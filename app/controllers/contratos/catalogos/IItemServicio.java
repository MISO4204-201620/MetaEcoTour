package controllers.contratos.catalogos;


import models.catalogo.ItemServicio;
import play.db.jpa.Transactional;

import java.util.List;

/**
 * Created by JoséLuis on 27/02/2016.
 */
public interface IItemServicio {

    public List<ItemServicio> getItemsByPaquetes(Long idPaquete);

    public List<ItemServicio> getItemsByServicios(Long idServicio);

    ItemServicio save(ItemServicio itemServicio);

    ItemServicio delete(Long idPaquete, Long idServicio);



}


