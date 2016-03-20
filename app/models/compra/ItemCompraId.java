package models.compra;

import java.io.Serializable;

/**
 * Created by JoséLuis on 19/03/2016.
 */
public class ItemCompraId  implements Serializable {
    Long idCompra;
    Long idProducto;

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }
}
