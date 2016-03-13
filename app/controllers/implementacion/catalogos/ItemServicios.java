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
    public List<models.catalogo.ItemServicio> getItemsByPaquetes(Long idPaquete) {
        return JPA.em().createNamedQuery("ItemServicio.findByPaquete", ItemServicio.class ).setParameter("idPaquete",idPaquete).getResultList();
    }

    @Override
    public List<models.catalogo.ItemServicio> getItemsByServicios(Long idServicio) {
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
    public models.catalogo.ItemServicio save(models.catalogo.ItemServicio itemServicio) {
        EntityManager em = JPA.em();
        ItemServicioId pKey = new ItemServicioId();
        pKey.setIdPaquete(itemServicio.getIdPaquete());
        pKey.setIdServicio(itemServicio.getIdServicio());
        ItemServicio itmSrv = em.find(ItemServicio.class, pKey);
        if(itmSrv == null){
            em.persist(itmSrv);
        }else{
            itmSrv.setCantidad(itemServicio.getCantidad());
            em.merge(itmSrv);
            itemServicio = itmSrv;
        }

        return itemServicio;
    }

    @Transactional
    public models.catalogo.ItemServicio delete(Long idPaquete, Long idServicio) {
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
