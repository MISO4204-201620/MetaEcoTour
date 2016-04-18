package controllers.contratos.catalogos;

import models.catalogo.Categoria;

import java.util.List;

/**
 * Created by jose on 2/20/16.
 */
public interface ICategorias {

    public List<Object> getCategorias();

    public Categoria getCategoriaById(long categoriaId);

    Categoria save(Categoria categoria);

    Categoria delete(long l);
}
