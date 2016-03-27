package models.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Camilo on 26/02/16.
 */

@Entity
@DiscriminatorValue("PAQ")
public class Paquete extends Producto{

    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idProducto")
    @JsonIgnore
    private List<ItemServicio> itemServicios;

    public List<ItemServicio> getItemServicios() {
        return itemServicios;
    }

    public void setItemServicios(List<ItemServicio> itemServicios) {
        this.itemServicios = itemServicios;
    }

    @Override
    public String getTipo() {
        return "PAQ";
    }
}
