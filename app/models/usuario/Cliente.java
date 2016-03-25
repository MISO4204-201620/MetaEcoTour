package models.usuario;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by manuel on 10/02/16.
 */
@Entity
public class Cliente extends Usuario implements Serializable{

    @Column(nullable=false)
    private String apellido;

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String getTipo() {
        return "Cliente";
    }
}
