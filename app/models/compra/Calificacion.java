package models.compra;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by JoséLuis on 13/03/2016.
 */
@Entity @IdClass(CalificacionId.class)
@NamedQueries({
        @NamedQuery(name = "Calificacion.findByUsuario", query = "SELECT r FROM Calificacion r WHERE r.idUsuario = :idUsuario order by r.fecha desc "),
        @NamedQuery(name = "Calificacion.findByServicio", query = "SELECT r FROM Calificacion r WHERE r.idProducto = :idServicio order by r.fecha desc")
})public class Calificacion {

    @Column(nullable=false)
    private Long idUsuario;
    @Column(nullable=false)
    private Long idProducto;
    @Column(nullable=false)
    private Date fecha  ;
    @Column(nullable=false)
    private int valor ;
    @Column(nullable=true)
    private String comentario  ;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
