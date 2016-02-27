package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.IRecurso;
import models.catalogo.Producto;
import models.catalogo.Recurso;
import play.db.jpa.JPA;

import java.util.List;

/**
 * Created by JoséLuis on 27/02/2016.
 */
public class Recursos implements IRecurso {

    @Override
    public List<Recurso> getRecursosByProd(Long prodId) {
        return JPA.em().createNamedQuery("Recurso.findAll", Recurso.class ).getResultList();
    }

    @Override
    public List<Recurso> getRecursosByProdByType(Long prodId, String tipo) {
        return null;
    }

    @Override
    public Recurso save(Recurso producto) {
        return null;
    }

    @Override
    public Recurso delete(long l) {
        return null;
    }
}
