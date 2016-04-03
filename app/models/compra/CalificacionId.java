package models.compra;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by JoséLuis on 13/03/2016.
 */
public class CalificacionId implements Serializable {
    private Long idUsuario;
    private Long idProducto;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

}
