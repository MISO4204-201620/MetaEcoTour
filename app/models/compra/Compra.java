package models.compra;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

/**
 * Created by JoséLuis on 19/03/2016.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Compra.findCompraAbiertaByUsuario", query = "SELECT r FROM Compra r WHERE r.idUsuario = :idUsuario and r.estado in ('O','E')  "),
        @NamedQuery(name = "Compra.findComprasByUsuario", query = "SELECT r FROM Compra r WHERE r.idUsuario = :idUsuario  order by r.idCompra desc "),
        @NamedQuery(name = "Compra.findByCompra", query = "SELECT r FROM Compra r WHERE r.idCompra = :idCompra  ")
})public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "compraGen")
    @SequenceGenerator(name = "compraGen",
            sequenceName = "compra_seq")
    private Long idCompra;
    @Column(nullable=false)
    private Long idUsuario;
    @Column(nullable=false)
    private Date FechaCreacion;
    @Column(nullable=true)
    private Date FechaActualizacion;
    /**
     * Estados soportados X:Cancelado, C:Cerrado, O:Abierto;
     */
    @Column(nullable=false)
    private String estado;
    @Column(nullable=false)
    private String medioPago;
    @Column(nullable=true)
    private String descripcion;

    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idCompra")
    @JsonIgnore
    private List<ItemCompra> itemCompras;


    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        FechaCreacion = fechaCreacion;
    }

    public Date getFechaActualizacion() {
        return FechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        FechaActualizacion = fechaActualizacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<ItemCompra> getItemCompras() {
        return itemCompras;
    }

    public void setItemCompras(List<ItemCompra> itemCompras) {
        this.itemCompras = itemCompras;
    }
}
