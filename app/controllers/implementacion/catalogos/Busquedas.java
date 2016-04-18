package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IBusqueda;
import models.catalogo.Busqueda;
import models.catalogo.Producto;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Camilo on 5/03/16.
 */
public class Busquedas implements IBusqueda {

    @Override
    public List<Busqueda> getBusquedas() {

        return JPA.em().createNamedQuery("Busqueda.findAll", Busqueda.class).getResultList();
    }

    @Override
    public List<Busqueda> getBusquedasByProductId(Long productId) {
        return JPA.em().createNamedQuery("Busqueda.findBusquedaByProductId", Busqueda.class).setParameter("productId", productId).getResultList();
    }

    @Override
    public List<Busqueda> getBusquedasByTypeAndDate(String tipo, Date fechaInicio, Date fechaFin) {

        String parameterSentence = "SELECT bu FROM Busqueda bu where bu.tipoBusqueda = :tipo and 1=1";


        if (fechaInicio != null && fechaFin != null) {
            parameterSentence = parameterSentence + " and bu.fechaBusqueda between :fechaInicio and :fechaFin";
        }
        Query query = JPA.em().createQuery(parameterSentence, Busqueda.class);
        query.setParameter("tipo", tipo);
        if (fechaInicio != null && fechaFin != null) {
            System.out.println("Entrando a la busqueda.....   " + fechaInicio + "     " + fechaFin);
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
        }

        return query.getResultList();
    }


    @Override
    public HashMap<String, String> getBusquedasByTypeDateAndCount(String tipo, Date fechaInicio, Date fechaFin) {

        HashMap<String, String> busquedas = new LinkedHashMap<String, String>();
        String parameterSentence = "SELECT count(bu.idproducto) total, bu.idproducto FROM Busqueda bu where bu.tipoBusqueda = :tipo and 1=1";

        if (fechaInicio != null && fechaFin != null) {
            parameterSentence = parameterSentence + " and bu.fechaBusqueda between :fechaInicio and :fechaFin";
        }


        parameterSentence = parameterSentence + " group BY bu.idproducto";
        Query query = JPA.em().createNativeQuery(parameterSentence);
        query.setParameter("tipo", tipo);
        if (fechaInicio != null && fechaFin != null) {
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
        }

        List<Object[]> resultados = query.getResultList();
        System.out.println(resultados.size());
        for (Object[] resultado : resultados) {
            busquedas.put(resultado[1].toString(), resultado[0].toString());
        }

        return busquedas;
    }


    @Override
    @Transactional
    public Busqueda save(Busqueda busqueda) {
        EntityManager em = JPA.em();
        if (busqueda.getId() == null) {
            try {
                em.persist(busqueda);
            } catch (PersistenceException e) {
                System.out.println("El producto no existe");
                busqueda = null;
            }
        } else {
            Busqueda busquedaTmp = em.find(Busqueda.class, busqueda.getId());

            if (busquedaTmp != null) {
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
        Busqueda busqueda = em.find(Busqueda.class, productId);
        em.remove(busqueda);
        return busqueda;
    }

    @Override
    @Transactional
    public void deleteAllSearchByProdId(Long productId) {

        EntityManager em = JPA.em();
        List<Busqueda> busquedasABorrar = JPA.em().createNamedQuery("Busqueda.findBusquedaByProductId", Busqueda.class).setParameter("productId", productId).getResultList();
        for (Busqueda busqueda : busquedasABorrar) {
            em.remove(busqueda);
        }
        em.flush();
    }
}
