package controllers.contratos.compras;

import models.compra.ItemCompra;

import java.util.List;

/**
 * Created by JoséLuis on 19/03/2016.
 */
public interface IItemCompra {

    public List<ItemCompra> getItemCompraByCompra(Long idCompra);
    public List<ItemCompra> getItemCompraByProducto(Long idProducto);

    ItemCompra save(ItemCompra itemCompra);
    ItemCompra delete(ItemCompra itemCompra);

}
