package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IProducto;
import models.catalogo.Categoria;
import models.catalogo.Paquete;
import models.catalogo.Producto;
import models.catalogo.Servicio;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

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
            System.out.println("La clase :  "+pro.getClass());
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
    public Producto save(Producto producto) {
        return null;
    }

    @Override
    public Producto delete(long l) {
        return null;
    }
}
