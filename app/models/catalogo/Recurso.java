package models.catalogo;

import javax.persistence.*;

/**
 * Created by JoséLuis on 27/02/2016.
 */
@Entity
@NamedQueries({
        @NamedQuery(name="Recurso.findAll", query="SELECT r FROM Recurso r")
})
public class Recurso {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "recursoGen")
    @SequenceGenerator(name = "recursoGen",
            sequenceName = "recurso_seq")
    private Long id;

    @Column(nullable=false)
    private String tipo;
    @Column(nullable=false)
    private String nombre;
    private String label;
    private String comentario;
    @Column(nullable=false)
    private String contenido;

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

    public void setContenido(String contenido) {
        this.contenido = contenido;
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

    public String getContenido() {
        return contenido;
    }
}
