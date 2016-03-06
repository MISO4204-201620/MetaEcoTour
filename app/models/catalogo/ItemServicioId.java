package models.catalogo;

import java.io.Serializable;

/**
 * Created by JoséLuis on 28/02/2016.
 */
public class ItemServicioId implements Serializable {
    Long idPaquete;
    Long idServicio;

    public Long getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(Long idPaquete) {
        this.idPaquete = idPaquete;
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }
}
