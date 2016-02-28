package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IItemServicio;
import play.db.jpa.JPA;
import models.catalogo.ItemServicio;

import java.util.List;

/**
 * Created by JoséLuis on 27/02/2016.
 */
public class ItemServicios implements IItemServicio {
    @Override
    public List<models.catalogo.ItemServicio> getItemsByPaquetes(Long idPaquete) {
        return JPA.em().createNamedQuery("ItemServicio.findByPaquete", ItemServicio.class ).setParameter("idPaquete",idPaquete).getResultList();
    }

    @Override
    public List<models.catalogo.ItemServicio> getItemsByServicios(Long idServicio) {
        return JPA.em().createNamedQuery("ItemServicio.findByServicio", ItemServicio.class ).setParameter("idServicio",idServicio).getResultList();
    }

    @Override
    public models.catalogo.ItemServicio save(models.catalogo.ItemServicio itemServicio) {
        return null;
    }

    @Override
    public models.catalogo.ItemServicio delete(Long idPaquete, Long idServicio) {
        return null;
    }
}
