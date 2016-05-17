package models.mensajeria;

import java.util.Date;

/**
 * Created by crimago on 21/03/16.
 */
public class ComentarioDTO {

    private long id;
    private String nombreUsuario;
    private String comentario;
    private Date fecha;
    private long numeroComentarios;
    private long origen;
    private long usuarioDestino;
    private Comentario.Tipo tipo;
    private long idUsuario;

    public ComentarioDTO() {

    }

    public ComentarioDTO(long id, String nombreUsuario, String comentario, long numeroComentarios, Date fecha) {
        this.nombreUsuario = nombreUsuario;
        this.comentario = comentario;
        this.numeroComentarios = numeroComentarios;
        this.fecha = fecha;
        this.id = id;
    }

    public ComentarioDTO(long id, String nombreUsuario, String comentario, long numeroComentarios, Date fecha, long idUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.comentario = comentario;
        this.numeroComentarios = numeroComentarios;
        this.fecha = fecha;
        this.id = id;
        this.idUsuario = idUsuario;
    }

    public ComentarioDTO(long id, String nombreUsuario, String comentario, long numeroComentarios, Date fecha, Comentario.Tipo tipo) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.comentario = comentario;
        this.tipo = tipo;
        this.fecha = fecha;
        this.numeroComentarios = numeroComentarios;
    }

    public ComentarioDTO(long id, String nombreUsuario, String comentario, Date fecha, long numeroComentarios, long origen, long usuarioDestino, Comentario.Tipo tipo) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.comentario = comentario;
        this.fecha = fecha;
        this.numeroComentarios = numeroComentarios;
        this.origen = origen;
        this.usuarioDestino = usuarioDestino;
        this.tipo = tipo;
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

    public long getOrigen() {
        return origen;
    }

    public void setOrigen(long origen) {
        this.origen = origen;
    }

    public Comentario.Tipo getTipo() { return tipo; }

    public void setTipo(Comentario.Tipo tipo) { this.tipo = tipo; }

    public long getUsuarioDestino() {
        return usuarioDestino;
    }

    public void setUsuarioDestino(long usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
