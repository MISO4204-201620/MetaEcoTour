package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IItemServicio;
import controllers.contratos.catalogos.IProducto;
import controllers.contratos.catalogos.IServicio;
import models.catalogo.Servicio;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by JoséLuis on 05/03/2016.
 */
public class Servicios implements IServicio {
    private static IProducto productos = new Productos();


    @Override
    public Servicio getServicioById(Long id) {
        Servicio Servicio = JPA.em().find(Servicio.class, id);
        return Servicio;
    }

    @Override
    public Servicio save(Servicio Servicio) {
        return null;
    }

    @Override
    public Servicio delete(Long id) {
        return (Servicio) productos.delete(id);
    }
}
