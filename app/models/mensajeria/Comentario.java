package models.mensajeria;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

/**
 * Created by manuel on 9/03/16.
 */
@Entity
@NamedQueries({
        @NamedQuery(name="Comentario.findByIdProducto", query="SELECT cm FROM Comentario cm where cm.idProducto = :productoId"),
        @NamedQuery(name="Comentario.findByIdUsuario", query="SELECT cm FROM Comentario cm where cm.idUsuario = :usuarioId")
})
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "comentarioGen")
    @SequenceGenerator(name = "comentarioGen",
            sequenceName = "comentario_seq")
    private long id;

    @Column(nullable=false)
    private String texto;

    @Column(nullable=false)
    private Date fecha;

    @Column(nullable=false)
    private long idUsuario;

    @ManyToOne
    private Comentario origen;

    @OneToMany(mappedBy="origen")
    private Collection<Comentario> subComentarios;

    @Column(nullable=false)
    private long idProducto;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Comentario getOrigen() {
        return origen;
    }

    public void setOrigen(Comentario origen) {
        this.origen = origen;
    }

    public Collection<Comentario> getSubComentarios() {
        return subComentarios;
    }

    public void setSubComentarios(Collection<Comentario> subComentarios) {
        this.subComentarios = subComentarios;
    }

    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }

    public long getIdProducto() { return idProducto; }

    public void setIdProducto(long idProducto) { this.idProducto = idProducto; }
}
