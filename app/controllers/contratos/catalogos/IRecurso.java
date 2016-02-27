package controllers.contratos.catalogos;

import java.util.List;
import models.catalogo.Recurso;

/**
 * Created by JoséLuis on 27/02/2016.
 */
public interface IRecurso {
    public List<Recurso> getRecursosByProd(Long prodId);

    public List<Recurso> getRecursosByProdByType(Long prodId, String tipo);


    Recurso save(Recurso producto);

    Recurso delete(long l);

}
