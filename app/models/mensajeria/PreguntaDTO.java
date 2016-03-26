package models.mensajeria;

import java.util.Date;

/**
 * Created by manuel on 25/03/16.
 */
public class PreguntaDTO {

    private long idPregunta;
    private String comentarioPregunta;
    private Date fechaPregunta;
    private long idRespuesta;
    private String comentarioRespuesta;
    private Date fechaRespuesta;

    public PreguntaDTO(){

    }

    public PreguntaDTO(long idPregunta, String comentarioPregunta, Date fechaPregunta) {
        this.idPregunta = idPregunta;
        this.comentarioPregunta = comentarioPregunta;
        this.fechaPregunta = fechaPregunta;
    }

    public PreguntaDTO(long idPregunta, String comentarioPregunta, Date fechaPregunta, long idRespuesta, String comentarioRespuesta, Date fechaRespuesta) {
        this.idPregunta = idPregunta;
        this.comentarioPregunta = comentarioPregunta;
        this.idRespuesta = idRespuesta;
        this.fechaPregunta = fechaPregunta;
        this.comentarioRespuesta = comentarioRespuesta;
        this.fechaRespuesta = fechaRespuesta;
    }

    public long getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(long idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getComentarioPregunta() {
        return comentarioPregunta;
    }

    public void setComentarioPregunta(String comentarioPregunta) {
        this.comentarioPregunta = comentarioPregunta;
    }

    public Date getFechaPregunta() {
        return fechaPregunta;
    }

    public void setFechaPregunta(Date fechaPregunta) {
        this.fechaPregunta = fechaPregunta;
    }

    public long getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(long idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public String getComentarioRespuesta() {
        return comentarioRespuesta;
    }

    public void setComentarioRespuesta(String comentarioRespuesta) {
        this.comentarioRespuesta = comentarioRespuesta;
    }

    public Date getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(Date fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }
}
