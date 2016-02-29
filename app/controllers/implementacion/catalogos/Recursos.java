package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IRecurso;
import models.catalogo.Recurso;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by JoséLuis on 27/02/2016.
 */
public class Recursos implements IRecurso {

    @Override
    public List<Recurso> getRecursosByProd(Long productId) {
        return JPA.em().createNamedQuery("Recurso.findByPrd", Recurso.class ).setParameter("productId",productId).getResultList();
    }

    @Override
    public List<Recurso> getRecursosByProdByType(Long productId, String tipo) {
        return JPA.em().createNamedQuery("Recurso.findByPrdByType", Recurso.class ).setParameter("productId",productId).setParameter("tipo",tipo).getResultList();
    }

    @Transactional
    public Recurso save(Recurso recurso) {
        EntityManager em = JPA.em();
        if (recurso.getId() == null){
            em.persist(recurso);
        } else {
            Recurso recTemp = em.find(Recurso.class, recurso.getId());

            if(recTemp != null){
                recTemp.setComentario(recurso.getComentario());
                recTemp.setContenido(recurso.getContenido());
                recTemp.setLabel(recurso.getLabel());
                recTemp.setNombre(recurso.getNombre());
                recTemp.setTipo(recurso.getTipo());
                em.merge(recTemp);
                return recTemp;
            }
        }

        return recurso;
    }

    @Override
    public Recurso delete(long id) {
        EntityManager em = JPA.em();
        Recurso recurso = em.find(Recurso.class, id);
        em.remove(recurso);
        return recurso;
    }
}
