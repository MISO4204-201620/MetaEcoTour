package models;

import javax.persistence.*;

/**
 * Created by manuel on 10/02/16.
 */
@Entity
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
