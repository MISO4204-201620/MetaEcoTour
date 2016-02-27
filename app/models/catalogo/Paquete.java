package models.catalogo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Camilo on 26/02/16.
 */

@Entity
@DiscriminatorValue("PAQ")
public class Paquete extends Producto{

}
