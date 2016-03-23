package models.catalogo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;

/**
 * Created by JoséLuis on 27/02/2016.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Recurso.findByPrd", query = "SELECT r FROM Recurso r WHERE r.idProducto = :productId"),
        @NamedQuery(name = "Recurso.findByPrdByType", query = "SELECT r FROM Recurso r WHERE r.idProducto = :productId and r.tipo = :tipo")
})
public class Recurso {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "recursoGen")
    @SequenceGenerator(name = "recursoGen",
            sequenceName = "recurso_seq")
    private Long id;

    @Column(nullable=false)
    private Long idProducto;
    @Column(nullable=false)
    private String tipo;
    @Column(nullable=false)
    private String nombre;
    private String label;
    private String comentario;
    @Column(nullable=false, columnDefinition = "text")//@Column(columnDefinition = "text")
    private String contenido;

    public Recurso(){
        super();
    }
    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getLabel() {
        return label;
    }

    public String getComentario() {
        return comentario;
    }

}
