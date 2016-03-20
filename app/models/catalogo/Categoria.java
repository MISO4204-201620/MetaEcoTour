package models.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by manuel on 10/02/16.
 */
@Entity
@NamedQueries({
        @NamedQuery(name="Categoria.findAll", query="SELECT p FROM Categoria p"),
        @NamedQuery(name="Categoria.findAllCantidadProductos", query="SELECT NEW models.catalogo.CategoriaDTO(c.nombre, COUNT(p)) FROM Categoria c LEFT JOIN c.productos p GROUP BY c")
})
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "categoriaGen")
    @SequenceGenerator(name = "categoriaGen",
            sequenceName = "categoria_seq")
    private long id;

    @Column(nullable=false)
    private String nombre;

    private long idMarketPlace;

    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idCategoria")
    @JsonIgnore
    private List<Producto> productos ;

    public List<Producto> getProductos() { return productos; }

    public void setProductos(List<Producto> productos) { this.productos = productos; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getIdMarketPlace() {
        return idMarketPlace;
    }

    public void setIdMarketPlace(long idMarketPlace) { this.idMarketPlace = idMarketPlace; }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

class CategoriaDTO {

    public String nombre;

    public long cantidad;

    public CategoriaDTO(String nombre, long cantidad) {
        this.cantidad = cantidad;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getCantidad() {
        return cantidad;
    }

    public void setCantidad(long cantidad) {
        this.cantidad = cantidad;
    }
}