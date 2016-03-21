package models.mensajeria;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by manuel on 9/03/16.
 */
@Entity
@NamedQueries({

        @NamedQuery(name="Comentario.findByIdProducto", query="SELECT cm FROM Comentario cm where cm.idProducto = :productoId and cm.origen IS NULL"),
        @NamedQuery(name="ComentarioDTO.findByIdProductoUsuario", query="SELECT new models.mensajeria.ComentarioDTO(cm.id, us.nombre, cm.texto, COUNT(scm), cm.fecha ) " +
                "FROM Usuario us, Comentario cm LEFT JOIN cm.subComentarios scm " +
                "where cm.idProducto = :productoId " +
                "and us.id = cm.idUsuario " +
                "and cm.origen IS NULL " +
                "GROUP BY cm, us.nombre"),
        @NamedQuery(name="ComentarioDTO.findComentariosByPadre", query="SELECT new models.mensajeria.ComentarioDTO(cm.id, us.nombre, cm.texto, COUNT(scm), cm.fecha ) " +
                "FROM Usuario us, Comentario cm LEFT JOIN cm.subComentarios scm " +
                "where cm.origen = :comentarioId " +
                "and us.id = cm.idUsuario " +
               // "and cm.origen IS NULL " +
                "GROUP BY cm, us.nombre"),
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

    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "origen")
    @JsonIgnore
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

class ComentarioDTO{
    private long id;
    private String nombreUsuario;
    private String comentario;
    private Date fecha;
    private long numeroComentarios;
    private List<ComentarioDTO> subComentarios;

    public ComentarioDTO(long id, String nombreUsuario, String comentario, long numeroComentarios, Date fecha) {
        this.nombreUsuario = nombreUsuario;
        this.comentario = comentario;
        this.numeroComentarios = numeroComentarios;
        this.fecha = fecha;
        this.id = id;
    }

    public ComentarioDTO(long id, String nombreUsuario, String comentario,  long numeroComentarios, Date fecha, List<ComentarioDTO> subComentarios) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.comentario = comentario;
        this.fecha = fecha;
        this.subComentarios = subComentarios;
        this.numeroComentarios = numeroComentarios;
    }

    public List<ComentarioDTO> getSubComentarios() {
        return subComentarios;
    }

    public void setSubComentarios(List<ComentarioDTO> subComentarios) {
        this.subComentarios = subComentarios;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
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

    public long getNumeroComentarios() {
        return numeroComentarios;
    }

    public void setNumeroComentarios(long numeroComentarios) {
        this.numeroComentarios = numeroComentarios;
    }


}