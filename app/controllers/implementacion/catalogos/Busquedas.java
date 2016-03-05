package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IBusqueda;
import models.catalogo.Busqueda;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by Camilo on 5/03/16.
 */
public class Busquedas implements IBusqueda {

    @Override
    public List<Busqueda> getBusquedas() {

        return JPA.em().createNamedQuery("Busqueda.findAll", Busqueda.class ).getResultList();
    }

    @Override
    public List<Busqueda> getBusquedasByProductId(Long productId) {
        return JPA.em().createNamedQuery("Busqueda.findBusquedaByProductId", Busqueda.class ).setParameter("productId",productId).getResultList();
    }

    @Override
    @Transactional
    public Busqueda save(Busqueda busqueda) {
        EntityManager em = JPA.em();
        if (busqueda.getId() == null){
            try {
                em.persist(busqueda);
            }catch (PersistenceException e){
                System.out.println("El producto no existe");
                busqueda=null;
            }
        } else {
            Busqueda busquedaTmp = em.find(Busqueda.class, busqueda.getId());

            if(busquedaTmp != null){
                busquedaTmp.setFechaBusqueda(busqueda.getFechaBusqueda());
                em.merge(busquedaTmp);
                return busquedaTmp;
            }
        }
        return busqueda;
    }

    @Override
    @Transactional
    public Busqueda delete(Long productId) {
        EntityManager em = JPA.em();
        Busqueda busqueda= em.find(Busqueda.class, productId);
        em.remove(busqueda);
        return busqueda;
    }

    @Override
    @Transactional
    public void deleteAllSearchByProdId(Long productId) {

        EntityManager em = JPA.em();
        List<Busqueda> busquedasABorrar = JPA.em().createNamedQuery("Busqueda.findBusquedaByProductId", Busqueda.class ).setParameter("productId",productId).getResultList();
        for(Busqueda busqueda :busquedasABorrar){
            em.remove(busqueda);
        }
        em.flush();
    }
}
