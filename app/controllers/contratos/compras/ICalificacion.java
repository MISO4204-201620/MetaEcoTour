package controllers.contratos.compras;

import models.compra.Calificacion;

import java.util.List;

/**
 * Created by JoséLuis on 13/03/2016.
 */
public interface ICalificacion {

    public List<Calificacion> getCalificacionByServicio(Long idServicio);
    public List<Calificacion> getCalificacionByUsuario(Long idUsuario);

    Calificacion save(Calificacion calificacion);

    Calificacion delete(Calificacion calificacion);

}
