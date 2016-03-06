package controllers.contratos.catalogos;

import models.catalogo.Servicio;

import java.util.List;

/**
 * Created by JoséLuis on 05/03/2016.
 */
public interface IServicio {

    public Servicio getServicioById(Long id);

    Servicio save(Servicio Servicio);

    Servicio delete(Long id);

}
