package models.catalogo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Created by Camilo on 26/02/16.
 */

@Entity
@DiscriminatorValue("PAQ")
public class Paquete extends Producto{

}
