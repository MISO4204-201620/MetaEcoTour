package models.mensajeria;

import java.util.Date;

/**
 * Created by crimago on 17/04/16.
 */
public class MensajeDTO {

    String id, texto, idusuario;
    Date fecha;

    public MensajeDTO(Date fecha, String id, String idusuario, String texto) {
        this.fecha = fecha;
        this.id = id;
        this.idusuario = idusuario;
        this.texto = texto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
