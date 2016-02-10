package models;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by manuel on 10/02/16.
 */
@Entity
public class Proveedor extends Usuario implements Serializable{

    @Column(nullable=false)
    private String descripcion;

    @Column(nullable=false)
    private Boolean activo;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
