package models.catalogo;

import javax.persistence.*;

/**
 * Created by JoséLuis on 27/02/2016.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "ItemServicio.findByPaquete", query = "SELECT is FROM ItemServicio is WHERE is.idPaquete = :idPaquete"),
        @NamedQuery(name = "ItemServicio.findByServicio", query = "SELECT is FROM ItemServicio is WHERE is.idServicio = :idServicio ")
})
public class ItemServicio {

    @Id @Column(nullable=false)
    private Long idPaquete;
    @Column(nullable=false)
    private Long idServicio;
    @Column(nullable=false)
    private int cantidad;

    public Long getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(Long idPaquete) {
        this.idPaquete = idPaquete;
    }

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
}
