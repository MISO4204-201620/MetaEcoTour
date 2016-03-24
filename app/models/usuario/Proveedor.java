package models.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.catalogo.Producto;
import models.catalogo.Recurso;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by manuel on 10/02/16.
 */
@Entity
public class Proveedor extends Usuario implements Serializable{

    @Column(nullable=false)
    private String descripcion;

    @Column(nullable=false)
    private Boolean activo;

    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idProveedor")
    @JsonIgnore
    private List<Producto> productos ;

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

    @Override
    public String getTipo() {
        return "Proveedor";
    }
}
