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
        @NamedQuery(name="ComentarioDTO.findByIdProductoUsuario", query="SELECT new models.mensajeria.ComentarioDTO(cm.id, us.nombre, cm.texto, COUNT(scm), cm.fecha , cm.idUsuario) " +
                "FROM Usuario us, Comentario cm LEFT JOIN cm.subComentarios scm " +
                "where cm.idProducto = :productoId " +
                "and us.id = cm.idUsuario " +
                "and cm.origen IS NULL " +
                "and cm.tipo = :tipo " +
                "GROUP BY cm, us.nombre " +
                "ORDER BY cm.fecha desc"),
        @NamedQuery(name="ComentarioDTO.findComentariosByPadre", query="SELECT new models.mensajeria.ComentarioDTO(cm.id, us.nombre, cm.texto, COUNT(scm), cm.fecha, cm.tipo ) " +
                "FROM Usuario us, Comentario cm LEFT JOIN cm.subComentarios scm " +
                "where cm.origen = :comentarioId " +
                "and us.id = cm.idUsuario " +
               // "and cm.origen IS NULL " +
                "GROUP BY cm, us.nombre"),
        @NamedQuery(name="Comentario.findByIdUsuario", query="SELECT cm FROM Comentario cm where cm.idUsuario = :usuarioId")
})
@SqlResultSetMapping(name="MensajeDTOMapping",
        classes = {
                @ConstructorResult(targetClass = MensajeDTO.class,
                        columns = {@ColumnResult(name="fecha", type = java.util.Date.class),
                                   @ColumnResult(name="id", type = String.class ),
                                   @ColumnResult(name="idusuario", type = String.class),
                                   @ColumnResult(name="texto", type = String.class)}
                )}
)
public class Comentario {

    public enum Tipo {
        COMENTARIO, PREGUNTA, MENSAJE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "comentarioGen")
    @SequenceGenerator(name = "comentarioGen",
            sequenceName = "comentario_seq")
    private long id;

    @Column(columnDefinition = "text")
    private String texto;

    @Column(nullable=false)
    private Date fecha;

    @Column(nullable=true)
    private long idUsuario;

    @Column(nullable=true)
    private Long idUsuarioDestino;

    @ManyToOne
    private Comentario origen;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Tipo tipo;

    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "origen")
    @JsonIgnore
    private Collection<Comentario> subComentarios;

    @Column(nullable=true)
    private Long idProducto;

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

    public Long getIdProducto() { return idProducto; }

    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }

    public long getIdUsuario() { return idUsuario; }

    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }

    public Tipo getTipo() { return tipo; }

    public void setTipo(Tipo tipo) { this.tipo = tipo; }

    public Comentario(long id) {
        this.id = id;
    }

    public Comentario() {

    }

    public Long getIdUsuarioDestino() {
        return idUsuarioDestino;
    }

    public void setIdUsuarioDestino(Long idUsuarioDestino) {
        this.idUsuarioDestino = idUsuarioDestino;
    }
}

