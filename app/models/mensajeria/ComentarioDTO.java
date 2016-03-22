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

    public ComentarioDTO() {

    }

    public ComentarioDTO(long id, String nombreUsuario, String comentario, long numeroComentarios, Date fecha) {
        this.nombreUsuario = nombreUsuario;
        this.comentario = comentario;
        this.numeroComentarios = numeroComentarios;
        this.fecha = fecha;
        this.id = id;
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
