package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IProducto;
import controllers.contratos.catalogos.IRecurso;
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
    private static IRecurso recursos= new Recursos();

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
            if(0==productoId){
                em.persist(producto);
            }else{
                producto=null;
            }

        }else{

            if(!"".equals(producto.getNombre())){
                productoTmp.setNombre(producto.getNombre());
            }
            if(!"".equals(producto.getDescripcion())){
                productoTmp.setDescripcion(producto.getDescripcion());
            }
            if( -1.0 !=producto.getPrecioActual()){
                productoTmp.setPrecioActual(producto.getPrecioActual());
            }
            if(-1 !=producto.getPuntuacion()){
                productoTmp.setPuntuacion(producto.getPuntuacion());
            }
            if(!"".equals(producto.getImagen())){
                productoTmp.setImagen(producto.getImagen());
            }

            em.merge(productoTmp);
            producto = productoTmp;
        }

        return producto;
    }

    @Override
    @Transactional
    public Producto delete(Long id) {
        EntityManager em = JPA.em();
        Producto producto = em.find(Producto.class, id);
        if(producto!=null) {
            recursos.deleteAllResourceByProdId(id);
            em.remove(producto);
        }
        return producto;
    }


}
