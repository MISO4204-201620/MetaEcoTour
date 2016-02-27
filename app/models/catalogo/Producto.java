package models.catalogo;

import javax.persistence.*;

/**
 * Created by Camilo on 26/02/16.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="PRODUCT_TYPE",discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PR")
@NamedQueries({
        @NamedQuery(name="Producto.findAll", query="SELECT pr FROM Producto pr")
        })
public abstract class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "productoGen")
    @SequenceGenerator(name = "productoGen",
            sequenceName = "producto_seq")
    private long id;

    @Column(nullable=false)
    private String nombre;

    private String descripcion;

    @Column(nullable=false)
    private double precioActual;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioActual() {
        return precioActual;
    }

    public void setPrecioActual(double precioActual) {
        this.precioActual = precioActual;
    }
}
