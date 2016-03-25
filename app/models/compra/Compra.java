package models.compra;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by JoséLuis on 19/03/2016.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Compra.findCompraAbiertaByUsuario", query = "SELECT r FROM Compra r WHERE r.idUsuario = :idUsuario and r.estado ='O'  "),
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
}
