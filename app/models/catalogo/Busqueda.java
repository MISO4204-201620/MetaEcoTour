package models.catalogo;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Camilo on 5/03/16.
 */
@Entity
@NamedQueries({
        @NamedQuery(name="Busqueda.findAll", query="SELECT bu FROM Busqueda bu"),
        @NamedQuery(name="Busqueda.findBusquedaByProductId", query="SELECT bu FROM Busqueda bu where bu.idProducto = :productId")
})
public class Busqueda {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "busquedaGen")
    @SequenceGenerator(name = "busquedaGen",
            sequenceName = "busqueda_seq")
    private Long id;
    @Column(nullable=false)
    private Long idProducto;
    @Column(nullable=false)
    private Date fechaBusqueda;


    public Busqueda(){
        super();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Long getId() {
        return id;
    }

    public Date getFechaBusqueda() {
        return fechaBusqueda;
    }

    public void setFechaBusqueda(Date fechaBusqueda) {
        this.fechaBusqueda = fechaBusqueda;
    }
}
