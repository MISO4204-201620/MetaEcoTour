package models.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import models.mensajeria.Comentario;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Camilo on 26/02/16.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="PRODUCT_TYPE",discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PR")
@NamedQueries({
        @NamedQuery(name="Producto.findAll", query="SELECT pr FROM Producto pr"),
        @NamedQuery(name="Producto.findProductById", query="SELECT pr FROM Producto pr where pr.id = :productId"),
        @NamedQuery(name="Producto.findProductByProveedorId", query="SELECT pr FROM Producto pr where pr.idProveedor = :idProveedor"),
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

    @Transient
    private int puntuacion;

    private String imagen;

    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idProducto")
    @JsonIgnore
    private List<Recurso> recursos ;

    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idProducto")
    @JsonIgnore
    private List<Busqueda> busquedasProducto ;

    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idProducto")
    @JsonIgnore
    private List<Comentario> comentarios ;

    private long idCategoria;

    @Column(nullable=false)
    private Long idProveedor;

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

    public double getPrecioActual() {return precioActual;}

    public long getIdCategoria() { return idCategoria; }

    public void setIdCategoria(long idCategoria) { this.idCategoria = idCategoria; }

    public void setPrecioActual(double precioActual) {
        this.precioActual = precioActual;
    }

    public List<Recurso> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<Recurso> recursos) {
        this.recursos = recursos;
    }

    public int getPuntuacion() { return puntuacion; }

    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }

    public String getImagen() { return imagen; }

    public void setImagen(String imagen) { this.imagen = imagen; }

    public List<Busqueda> getBusquedasProducto() {
        return busquedasProducto;
    }

    public void setBusquedasProducto(List<Busqueda> busquedasProducto) {
        this.busquedasProducto = busquedasProducto;
    }

    public List<Comentario> getComentarios() { return comentarios; }

    public void setComentarios(List<Comentario> comentarios) { this.comentarios = comentarios; }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }
}
