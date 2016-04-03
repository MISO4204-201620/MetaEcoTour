package controllers.contratos.compras;

import models.compra.Calificacion;

import java.util.List;

/**
 * Created by JoséLuis on 13/03/2016.
 */
public interface ICalificacion {

    public List<Calificacion> getCalificacionByServicio(Long idServicio);
    public List<Calificacion> getCalificacionByUsuario(Long idUsuario);

    public Calificacion getPromedioByServicio(Long idServicio);

    public boolean validaCompraProducto(Long idUsuario, Long idProducto);

    Calificacion save(Calificacion calificacion);

    Calificacion delete(Calificacion calificacion);

}
