package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IAtributo;
import models.catalogo.Atributo;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by JoséLuis on 25/03/2016.
 */
public class Atributos implements IAtributo {
    @Transactional
    public List<Atributo> getAtributoByProducto(Long idProducto) {
        return JPA.em().createNamedQuery("Atributo.findByProductId", Atributo.class ).setParameter("idProducto",idProducto).getResultList();
    }

    @Transactional
    public Atributo save(Atributo atributo) {
        EntityManager em = JPA.em();
        Atributo itmSrv = new Atributo();
        itmSrv.setIdAtributo(atributo.getIdAtributo());
        itmSrv = em.find(Atributo.class, itmSrv);
        if(itmSrv == null){
            em.persist(itmSrv);
        }else{
            itmSrv.setValor(atributo.getValor());
            em.merge(itmSrv);
            atributo = itmSrv;
        }

        return atributo;
    }

    @Transactional
    public Atributo delete(Atributo atributo) {
        EntityManager em = JPA.em();
        Atributo itmSrv = new Atributo();
        itmSrv.setIdAtributo(atributo.getIdAtributo());
        itmSrv = em.find(Atributo.class, itmSrv);
        if(itmSrv!=null) {
            em.remove(itmSrv);
        }
        return itmSrv;
    }
}
