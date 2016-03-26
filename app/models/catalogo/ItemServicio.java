package models.catalogo;

import javax.persistence.*;

/**
 * Created by JoséLuis on 27/02/2016.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "ItemServicio.findByPaquete", query = "SELECT r FROM ItemServicio r WHERE r.idProducto = :idProducto "),
        @NamedQuery(name = "ItemServicio.findByServicio", query = "SELECT r FROM ItemServicio r WHERE r.idServicio = :idServicio ")
})
public class ItemServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "itemservGen")
    @SequenceGenerator(name = "itemservGen",
            sequenceName = "itemserv_seq")
    private Long id;

    @Column(nullable=false)
    private Long idProducto;
    @Column(nullable=false)
    private Long idServicio;

    @Column(nullable=false)
    private int cantidad;

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }
}


