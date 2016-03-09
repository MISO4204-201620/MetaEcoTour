package models.mensajeria;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

/**
 * Created by manuel on 9/03/16.
 */
@Entity
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
    private Long idUsuario;

    @ManyToOne
    private Comentario origen;

    @OneToMany(mappedBy="parent")
    private Collection<Comentario> subComentarios;

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
}
