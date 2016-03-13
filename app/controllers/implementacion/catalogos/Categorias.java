package controllers.implementacion.catalogos;

import controllers.contratos.catalogos.ICategorias;
import models.catalogo.Categoria;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by jose on 2/20/16.
 */
public class Categorias  implements ICategorias {


    public  List<Object> getCategorias() {
        return JPA.em().createNamedQuery("Categoria.findAllCantidadProductos").getResultList();
    }

    @Override
    @Transactional
    public Categoria save(Categoria categoria) {
        EntityManager em = JPA.em();
        Long categoriaId = categoria.getId();
        Categoria categoriaTemp = em.find(Categoria.class, categoriaId);

        if(categoriaTemp == null){
            em.persist(categoria);
        }else{
            categoriaTemp.setNombre(categoria.getNombre());
            em.merge(categoriaTemp);
            categoria = categoriaTemp;
        }

        return categoria;
    }

    @Override
    public Categoria delete(long id) {
        EntityManager em = JPA.em();
        Categoria categoria = em.find(Categoria.class, id);
        em.remove(categoria);
        return categoria;
    }
}
