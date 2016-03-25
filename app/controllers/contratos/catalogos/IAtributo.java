package controllers.contratos.catalogos;

import models.catalogo.Atributo;

import java.util.List;

/**
 * Created by JoséLuis on 25/03/2016.
 */
public interface IAtributo {

    public List<Atributo> getAtributoByProducto(Long idProducto);

    Atributo save(Atributo atributo);

    Atributo delete(Atributo atributo);

}
