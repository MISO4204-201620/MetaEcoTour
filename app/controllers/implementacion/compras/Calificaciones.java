package controllers.implementacion.compras;

import controllers.contratos.compras.ICalificacion;
import controllers.contratos.compras.ICompra;
import models.catalogo.Producto;
import models.compra.Calificacion;
import models.compra.CalificacionId;
import models.compra.Compra;
import models.compra.ItemCompra;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Date;
import java.util.List;

/**
 * Created by JoséLuis on 13/03/2016.
 */
public class Calificaciones implements ICalificacion {
    private static ICalificacion calificaciones = new Calificaciones();
    private static ICompra compras = new Compras();

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
        Calificacion calf = em.find(Calificacion.class, pKey);
        if(calf == null){
            calificacion.setFecha(new Date(System.currentTimeMillis()));
            em.persist(calificacion);
        }
        else
        {
            calf.setFecha(new Date(System.currentTimeMillis()));
            calf.setValor(calificacion.getValor());
        }
        //Traer todas las calificacionesy promediarlas...
        List<Calificacion> califica = calificaciones.getCalificacionByServicio(calificacion.getIdProducto());
        long sumatoria = 0;
        for (int i = 0; i < califica.size(); i++)
        {
            sumatoria += califica.get(i).getValor();
        }
        int promedio = Math.round(sumatoria / califica.size());
        Producto productoTmp = em.find(Producto.class, calificacion.getIdProducto());
        if(productoTmp != null)
        {
            productoTmp.setPuntuacion(promedio);
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
        Calificacion calf = em.find(Calificacion.class, pKey);
        if(calf!=null) {
            em.remove(calf);
        }
        return calf;
    }

    public boolean validaCompraProducto(Long idUsuario, Long idProducto){
        boolean comprado=false;

        List<Compra> comprasUsr = compras.getComprasByUsuario(idUsuario);
        for (int i = 0; i < comprasUsr.size(); i++)
        {
            if (comprasUsr.get(i).getEstado().compareTo("C") == 0) {
                List<ItemCompra> items = compras.getItemmsCompra(comprasUsr.get(i).getIdCompra());
                for (int j = 0; j < items.size(); j++)
                {
                   if(items.get(j).getIdProducto().compareTo(idProducto) == 0){
                        return true;
                    }
                }
            }
        }

        return comprado;
    }

}
