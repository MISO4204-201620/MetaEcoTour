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
        Long recId = recurso.getId();
        Recurso recTemp = JPA.em().find(Recurso.class, recId);

        if(recTemp == null){
            JPA.em().persist(recurso);
        }else{
            //update
        }

        return recurso;
    }

    @Override
    public Recurso delete(long l) {
        return null;
    }
}
