package models.catalogo;

import javax.persistence.*;

/**
 * Created by Camilo on 26/02/16.
 */

@Entity
@DiscriminatorValue("SER")
public class Servicio extends Producto {

    @Column(nullable=true)
    private String descDetallada;
    @Column(nullable=true)
    private String incluye;
    @Column(nullable=true)
    private String sugerencias;

    public String getDescDetallada() {
        return descDetallada;
    }

    public void setDescDetallada(String descDetallada) {
        this.descDetallada = descDetallada;
    }

    public String getIncluye() {
        return incluye;
    }

    public void setIncluye(String incluye) {
        this.incluye = incluye;
    }

    public String getSugerencias() {
        return sugerencias;
    }

    public void setSugerencias(String sugerencias) {
        this.sugerencias = sugerencias;
    }
}
