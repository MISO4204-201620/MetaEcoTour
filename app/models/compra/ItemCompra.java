package models.compra;

import javax.persistence.*;

/**
 * Created by JoséLuis on 19/03/2016.
 */
@Entity
@IdClass(ItemCompraId.class)
@NamedQueries({
        @NamedQuery(name = "ItemCompra.findByItemCompra", query = "SELECT r FROM ItemCompra r WHERE r.idCompra = :idCompra order by r.idProducto desc "),
        @NamedQuery(name = "ItemCompra.findByProducto", query = "SELECT r FROM ItemCompra r WHERE r.idProducto = :idProducto order by r.idCompra desc")
})public class ItemCompra {
    @Id
    @Column(nullable=false)
    private Long idCompra;
    @Column(nullable=false)
    private Long idProducto;
    @Column(nullable=false)
    private int cantidad;
    @Column(nullable=false)
    private double precio;


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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
