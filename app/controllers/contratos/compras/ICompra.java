package controllers.contratos.compras;

import models.compra.Compra;
import models.compra.ItemCompra;

import java.util.List;

/**
 * Created by JoséLuis on 19/03/2016.
 */
public interface ICompra {
    public List<Compra> getCompra(Long idCompra);
    public List<Compra> getCompraActivaByUsuario(Long idUsuario);

    public List<ItemCompra> getItemmsCompra(Long idCompra);
    public ItemCompra addItemmsCompra(ItemCompra itemCompra);
    public ItemCompra removeItemmsCompra(ItemCompra itemCompra);


    Compra save(Compra compra);
    Compra delete(Compra compra);

}
