package controllers.implementacion.compras;

import controllers.contratos.compras.ICalificacion;
import models.compra.Calificacion;
import models.compra.CalificacionId;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by JoséLuis on 13/03/2016.
 */
public class Calificaciones implements ICalificacion {
    private static ICalificacion calificacion = new Calificaciones();
    @Override
    @Transactional
    public List<Calificacion> getCalificacionByServicio(Long idServicio) {
        return JPA.em().createNamedQuery("Calificacion.findByServicio", Calificacion.class ).setParameter("idServicio",idServicio).getResultList();
    }

    @Override
    @Transactional
    public List<Calificacion> getCalificacionByUsuario(Long idUsuario) {
        return JPA.em().createNamedQuery("Calificacion.findByUsuario", Calificacion.class ).setParameter("idUsuario",idUsuario).getResultList();
    }

    @Override
    public Calificacion getPromedioByServicio(Long idServicio) {
        return JPA.em().createNamedQuery("Calificacion.findPromedioByServicio", Calificacion.class ).setParameter("idServicio",idServicio).getSingleResult();
    }

    @Override
    @Transactional
    public Calificacion save(Calificacion calificacion) {
        EntityManager em = JPA.em();
        CalificacionId pKey = new CalificacionId();
        pKey.setIdProducto(calificacion.getIdProducto());
        pKey.setIdUsuario(calificacion.getIdUsuario());
        pKey.setFecha(calificacion.getFecha());
        Calificacion calf = em.find(Calificacion.class, pKey);
        if(calf == null){
            em.persist(calf);
        }
        return calificacion;
    }

    @Override
    @Transactional
    public Calificacion delete(Calificacion calificacion) {
        EntityManager em = JPA.em();
        CalificacionId pKey = new CalificacionId();
        pKey.setIdProducto(calificacion.getIdProducto());
        pKey.setIdUsuario(calificacion.getIdUsuario());
        pKey.setFecha(calificacion.getFecha());
        Calificacion calf = em.find(Calificacion.class, pKey);
        if(calf!=null) {
            em.remove(calf);
        }
        return calf;
    }
}
