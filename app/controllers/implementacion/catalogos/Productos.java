package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IProducto;
import models.catalogo.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Camilo on 27/02/16.
 */
public class Productos implements IProducto {

    @Override
    public List<Producto> getProductos() {

        return JPA.em().createNamedQuery("Producto.findAll", Producto.class ).getResultList();
    }

    @Override
    public List<Producto> getProductosByType(String productType) {
        List<Producto> productos = JPA.em().createNamedQuery("Producto.findAll", Producto.class ).getResultList();
        List<Producto> productsByType = new ArrayList<Producto>();
        for (Producto pro : productos ) {

            if("PAQ".equals(productType)) {
                if (pro.getClass().equals(Paquete.class)) {
                    productsByType.add(pro);
                }
            }
            if("SER".equals(productType)) {
                if (pro.getClass().equals(Servicio.class)) {
                    productsByType.add(pro);
                }
            }
        }
        return productsByType;
    }

    @Override
    @Transactional
    public Producto getProductById(Long id){
        Producto producto = JPA.em().find(Producto.class, id);
        return producto;
    }

    @Override
    @Transactional
    public Producto save(Producto producto) {
        EntityManager em = JPA.em();
        Long productoId = producto.getId();

        Producto productoTmp = em.find(Producto.class, productoId);

        if(productoTmp == null){
            em.persist(producto);
        }else{
            System.out.println("Actualizando....");
            productoTmp.setNombre(productoTmp.getNombre());
            em.merge(productoTmp);
            producto = productoTmp;
        }

        return producto;
    }

    @Override
    public Producto delete(long l) {
        return null;
    }


}
