package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IItemServicio;
import controllers.contratos.catalogos.IServicio;
import models.catalogo.ItemServicioId;
import models.catalogo.Servicio;
import play.db.jpa.JPA;
import models.catalogo.ItemServicio;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by JoséLuis on 27/02/2016.
 */
public class ItemServicios implements IItemServicio {
    private static IServicio servicios = new Servicios();

    @Override
    public List<ItemServicio> getItemsByPaquetes(Long idPaquete) {
        return JPA.em().createNamedQuery("ItemServicio.findByPaquete", ItemServicio.class ).setParameter("idProducto",idPaquete).getResultList();
    }

    @Override
    public List<ItemServicio> getItemsByServicios(Long idServicio) {
        return JPA.em().createNamedQuery("ItemServicio.findByServicio", ItemServicio.class ).setParameter("idServicio",idServicio).getResultList();
    }

    @Override
    public List<Servicio> getServicioByPaquetes(Long idPaquete) {
        List<models.catalogo.ItemServicio> itmsServ = this.getItemsByPaquetes(idPaquete);
        List<Servicio> servList = new ArrayList<Servicio>();
        Iterator iter = itmsServ.iterator();
        while(iter.hasNext()){
            ItemServicio itmServ = (ItemServicio) iter.next();
            servList.add(servicios.getServicioById(itmServ.getIdServicio()));
        }
        return servList;
    }

    @Transactional
    public ItemServicio save(ItemServicio itemServicio) {

        EntityManager em = JPA.em();
        if (itemServicio.getId() == null){
            em.persist(itemServicio);
        } else {
            ItemServicio itemTemp = em.find(ItemServicio.class, itemServicio.getId());

            if(itemTemp != null){
                itemTemp.setCantidad(itemServicio.getCantidad());
                itemTemp.setIdServicio(itemServicio.getIdServicio());
                em.merge(itemTemp);
                return itemTemp;
            }
        }

        return itemServicio;
    }

    @Transactional
    public ItemServicio delete(Long idPaquete, Long idServicio) {
        EntityManager em = JPA.em();
        ItemServicioId pKey = new ItemServicioId();
        pKey.setIdPaquete(idPaquete);
        pKey.setIdServicio(idServicio);
        ItemServicio itmSrv = em.find(ItemServicio.class, pKey);
        if(itmSrv!=null) {
            em.remove(itmSrv);
        }
        return itmSrv;
    }
}
